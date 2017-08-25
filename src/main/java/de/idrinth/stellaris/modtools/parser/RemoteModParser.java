/*
 * Copyright (C) 2017 Idrinth
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
package de.idrinth.stellaris.modtools.parser;

import de.idrinth.stellaris.modtools.MainApp;
import de.idrinth.stellaris.modtools.access.DatabaseRetriever;
import de.idrinth.stellaris.modtools.access.Queue;
import de.idrinth.stellaris.modtools.entity.Modification;
import java.io.IOException;
import java.util.HashSet;
import javax.persistence.EntityManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class RemoteModParser implements Runnable {

    protected final String id;

    public RemoteModParser(String id) {
        this.id = id;
    }

    @Override
    public void run() {
        EntityManager manager = MainApp.entityManager.createEntityManager();
        if (!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        try {
            Modification mod = DatabaseRetriever.getModification(id);
            if ("".equals(mod.getName())) {//not already processed
                fill(mod);
                manager.persist(mod);
            }
            manager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            manager.getTransaction().rollback();
        }
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

    protected void fill(Modification mod) {
        try {
            Document doc = Jsoup.connect("http://steamcommunity.com/sharedfiles/filedetails/?id=" + id.replace("ugc_", "")).get();
            mod.setId(Integer.parseInt(id.replace("ugc_", ""), 10));
            if (null != doc.getElementById("highlightContent")) {
                mod.setDescription(clean(doc.getElementById("highlightContent")).html());
            }
            if (null != doc.getElementsByClass("workshopItemTitle").first()) {
                mod.setName(doc.getElementsByClass("workshopItemTitle").first().text());
            }
            if (null != doc.getElementById("highlightContent")) {
                mod.setDescription(clean(doc.getElementById("highlightContent")).html());
            }
            if (null != doc.getElementById("RequiredItems")) {
                HashSet<Modification> overwrite = new HashSet<>();
                doc.getElementById("RequiredItems").getElementsByTag("a").stream().filter((a) -> (a.hasAttr("href"))).map((a) -> "ugc_" + a.attributes().get("href").replaceAll("^.*id=([0-9]+).*$", "$1")).forEachOrdered((lId) -> {
                    Modification lMod = (Modification) DatabaseRetriever.get(Modification.class, lId);
                    Queue.add(new RemoteModParser(lId));//remote file names start with that string
                    overwrite.add(lMod);
                });
                mod.setOverwrite(overwrite);
            }
        } catch (IOException ex) {
        }
    }
}
