package de.idrinth.stellaris.modtools.access;

import de.idrinth.stellaris.modtools.entity.Mod;
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
        try {
            Document doc = Jsoup.connect("http://steamcommunity.com/sharedfiles/filedetails/?id="+mod.getId()).get();
            mod.setDescription(doc.getElementById("highlightContent").outerHtml());
            doc.getElementById("RequiredItems").getElementsByTag("a").stream().filter((element) -> (element.hasAttr("href"))).map((element) -> Integer.parseInt(element.attributes().get("href").replaceAll("/^.*id=([0-9]+).*4/", "$1"),10)).map((id) -> {
                if(!mod.getList().hasId(id)) {
                    Mod lMod = new Mod(mod.getList());
                    lMod.setId(id);
                    lMod.setName("Missing Mod "+id);
                    lMod.broken = true;
                    lMod.lock();
                }
                return id;
            }).forEachOrdered((id) -> {
                mod.getOverwrites().add(mod.getList().getById(id));
            });
        } catch (IOException ex) {
            Logger.getLogger(SteamDescription.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
