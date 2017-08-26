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
package de.idrinth.stellaris.modtools.step;

import de.idrinth.stellaris.modtools.MainApp;
import de.idrinth.stellaris.modtools.access.Queue;
import de.idrinth.stellaris.modtools.entity.Modification;
import de.idrinth.stellaris.modtools.step.abstracts.TaskList;
import java.io.IOException;
import javax.persistence.EntityManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class RemoteModParser extends TaskList {

    protected final int id;

    public RemoteModParser(int id, Queue queue) {
        super(queue);
        this.id = id;
    }

    @Override
    public void fill() {
        EntityManager manager = MainApp.entityManager.createEntityManager();
        if (!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        try {
            Modification mod = (Modification) manager.createNamedQuery("modifications.id",Modification.class).setParameter("id", id).getSingleResult();
            if(null == mod) {
                mod = new Modification();
                mod.setId(id);
            }
            if (null == mod.getDescription() || "".equals(mod.getDescription())) {//not already processed
                filli(mod);
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

    protected void filli(Modification mod) {
        try {
            Document doc = Jsoup.connect("http://steamcommunity.com/sharedfiles/filedetails/?id=" + String.valueOf(id)).get();
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
                doc.getElementById("RequiredItems").getElementsByTag("a").stream().filter((a) -> (a.hasAttr("href"))).map((a) -> "ugc_" + a.attributes().get("href").replaceAll("^.*id=([0-9]+).*$", "$1")).forEachOrdered((lId) -> {
                    Modification lMod = (Modification) MainApp.entityManager.createEntityManager().createNamedQuery("modifications.id",Modification.class).setParameter("id", lId).getSingleResult();
                    if(null == lMod) {
                        lMod = new Modification();
                        lMod.setId(Integer.parseInt(lId));
                        tasks.add(new RemoteModParser(lMod.getId(),queue));
                    }
                    mod.getOverwrite().add(lMod);
                });
            }
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
}
