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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        EntityManager manager = getEntityManager();
        if (!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        Modification mod = (Modification) manager.createNamedQuery("modifications.id",Modification.class).setParameter("id", id).getSingleResult();
        if(null == mod) {
            System.out.println("Creating new mod for remote id "+id);
            mod = new Modification("", id);
            mod.getCollides().setModification(mod);
            manager.persist(mod);
        }
        if (null == mod.getDescription() || "".equals(mod.getDescription())) {//not already processed
            filli(mod);
        }
        manager.getTransaction().commit();
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
    /**
     * 0.1-10s of waiting
     * @return 
     */
    private int getRandom() {
        return (int)(Math.random()*9900)+100;
    }
    private Document getDocument() throws IOException {
        try {
            Thread.sleep(getRandom());
        } catch (InterruptedException ex) {
            Logger.getLogger(RemoteModParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Jsoup
                .connect("http://steamcommunity.com/sharedfiles/filedetails/?id=" + String.valueOf(id))
                .userAgent("IdrinthStellarisModTools/v"+MainApp.version+" (https://github.com/Idrinths-Stellaris-Mods)")
                .timeout(10000)//10s
                .get();
    }
    protected void filli(Modification mod) {
        try {
            Document doc = getDocument();
            if (null != doc.getElementById("highlightContent")) {
                mod.setDescription(clean(doc.getElementById("highlightContent")).html());
                System.out.println("Added description for remote id "+id);
            }
            if (null != doc.getElementsByClass("workshopItemTitle").first()) {
                mod.setName(doc.getElementsByClass("workshopItemTitle").first().text());
                System.out.println("Added title for remote id "+id);
            }
            if (null != doc.getElementById("RequiredItems")) {
                Pattern reg = Pattern.compile("https?://(www\\.)?steamcommunity\\.com/workshop/filedetails/\\?id=([0-9]+).*");
                for(Element link:doc.getElementById("RequiredItems").getElementsByTag("a")) {
                    if(link.hasAttr("href")) {
                        int lId = match(reg.matcher(link.attr("href")));
                        if(lId>0) {
                            Modification lMod = (Modification) getEntityManager().createNamedQuery("modifications.id",Modification.class).setParameter("id", lId).getSingleResult();
                            if(null == lMod) {
                                lMod = new Modification("",lId);
                                tasks.add(new RemoteModParser(lMod.getId(),queue));
                                getEntityManager().persist(lMod);
                            }
                            mod.getOverwrite().add(lMod);
                        }
                    }
                }
                System.out.println("Added required items for remote id "+id);
            }
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
    private int match(Matcher matcher) {
        try {
            if(matcher.find()) {
                String result = matcher.group(2);
                return Integer.parseInt(result,10);
            }
        } catch(NumberFormatException e) {
            System.out.println("failed matching number in link");
        }
        return -1;
    }
    @Override
    protected String getIdentifier() {
        return String.valueOf(id);
    }
}
