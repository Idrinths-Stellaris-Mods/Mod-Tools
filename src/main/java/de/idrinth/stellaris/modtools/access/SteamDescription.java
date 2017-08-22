package de.idrinth.stellaris.modtools.access;

import de.idrinth.stellaris.modtools.model.Mod;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SteamDescription implements Runnable{
    protected Mod mod;

    public SteamDescription(Mod mod) {
        this.mod = mod;
    }
    @Override
    public void run() {
        System.out.println("Mod loading: "+mod.getId());
        try {
            Document doc = Jsoup.connect("http://steamcommunity.com/sharedfiles/filedetails/?id="+mod.getId()).get();
            mod.setDescription(doc.getElementById("highlightContent").html());
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
