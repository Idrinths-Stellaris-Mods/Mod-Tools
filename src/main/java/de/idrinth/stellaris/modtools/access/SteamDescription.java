/* 
 * Copyright (C) 2017 Björn Büttner
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
