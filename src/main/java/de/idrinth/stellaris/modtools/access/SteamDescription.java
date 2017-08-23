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
            mod.setDescription(clean(doc.getElementById("highlightContent")).html());
            if(null == doc.getElementById("RequiredItems")) {
                return;
            }
            doc.getElementById("RequiredItems").getElementsByTag("a").stream().filter((element) -> (element.hasAttr("href"))).map((element) -> Integer.parseInt(element.attributes().get("href").replaceAll("/^.*id=([0-9]+).*4/", "$1"),10)).map((id) -> {
                    Mod lMod = new Mod(mod.getList());
                    lMod.setId(id);
                    lMod.broken = true;
                    lMod.lock();
                    return id;
            });
        } catch (IOException ex) {
            Logger.getLogger(SteamDescription.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
