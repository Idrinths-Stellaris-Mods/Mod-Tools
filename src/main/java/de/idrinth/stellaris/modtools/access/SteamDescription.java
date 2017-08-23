package de.idrinth.stellaris.modtools.access;

import de.idrinth.stellaris.modtools.model.Mod;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class SteamDescription implements Runnable{
    protected Mod mod;

    public SteamDescription(Mod mod) {
        this.mod = mod;
    }
    protected Element clean(Element element) {
        Element work = element.clone();
        work.getElementsByTag("img").forEach((el) -> {
            el.remove();
        });
        work.getElementsByTag("script").forEach((el) -> {
            el.remove();
        });
        work.getElementsByTag("link").forEach((el) -> {
            el.remove();
        });
        work.getElementsByTag("canvas").forEach((el) -> {
            el.remove();
        });
        work.getElementsByAttribute("style").forEach((el) -> {
            el.removeAttr("style");
        });
        work.getElementsByAttribute("class").forEach((el) -> {
            el.removeAttr("class");
        });
        work.getElementsByAttribute("target").forEach((el) -> {
            el.removeAttr("target");
        });
        return work;
    }
    @Override
    public void run() {
        System.out.println("Mod loading: "+mod.getId());
        try {
            Document doc = Jsoup.connect("http://steamcommunity.com/sharedfiles/filedetails/?id="+mod.getId()).get();
            if(null != doc.getElementById("highlightContent")) {
                mod.setDescription(clean(doc.getElementById("highlightContent")).html());
            }
            System.out.println(doc.getElementById("RequiredItems"));
            if(null != doc.getElementById("RequiredItems")) {
                doc.getElementById("RequiredItems").getElementsByTag("a").stream().filter((a) -> (a.hasAttr("href"))).map((a) -> Integer.parseInt(a.attributes().get("href").replaceAll("^.*id=([0-9]+).*$", "$1"),10)).filter((id) -> (id>0)).map((id) -> {
                    Mod lMod = new Mod(mod.getList());
                    lMod.setId(id);
                    return lMod;
                }).map((lMod) -> {
                    lMod.broken = true;
                    return lMod;
                }).forEachOrdered((lMod) -> {
                    lMod.lock();
                });
            }
        } catch (IOException ex) {
            Logger.getLogger(SteamDescription.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
