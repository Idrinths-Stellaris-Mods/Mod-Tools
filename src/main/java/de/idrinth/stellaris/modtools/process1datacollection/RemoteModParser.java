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
package de.idrinth.stellaris.modtools.process1datacollection;

import de.idrinth.stellaris.modtools.MainApp;
import de.idrinth.stellaris.modtools.persistence.entity.Colliding;
import de.idrinth.stellaris.modtools.persistence.entity.Modification;
import de.idrinth.stellaris.modtools.process.ProcessTask;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import javax.persistence.NoResultException;

class RemoteModParser implements ProcessTask {

    protected final int id;
    protected final ArrayList<ProcessTask> todo = new ArrayList<>();

    public RemoteModParser(int id) {
        this.id = id;
    }

    protected String clean(Element element) {
        Element work = element.clone();
        String[] unwantedElements = "img,script,style,link,canvas".split(",");
        String[] unwantedAttributes = "style,class,target,id,src".split(",");
        for (String tag : unwantedElements) {
            work.getElementsByTag(tag).forEach((el) -> {
                el.remove();
            });
        }
        for (String attr : unwantedAttributes) {
            work.getElementsByAttribute(attr).forEach((el) -> {
                el.removeAttr(attr);
            });
        }
        return work.html().replaceAll("\\s{2,}", " ");
    }

    protected Document getDocument() throws IOException {
        try {
            Thread.sleep((int) (Math.random() * 900) + 100);
        } catch (InterruptedException ex) {
            Logger.getLogger(RemoteModParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Jsoup
                .connect("http://steamcommunity.com/sharedfiles/filedetails/?id=" + String.valueOf(id))
                .userAgent("IdrinthStellarisModTools/v" + MainApp.version + " (https://github.com/Idrinths-Stellaris-Mods)")
                .timeout(10000)//10s
                .get();
    }

    protected void fill(Modification mod, EntityManager manager) {
        try {
            Document doc = getDocument();
            if (null != doc.getElementById("highlightContent")) {
                mod.setDescription(clean(doc.getElementById("highlightContent")));
                System.out.println("Added description for remote id " + id);
            }
            if (null != doc.getElementsByClass("workshopItemTitle").first()) {
                mod.setName(doc.getElementsByClass("workshopItemTitle").first().text());
                System.out.println("Added title for remote id " + id);
            }
            if (null != doc.getElementById("RequiredItems")) {
                Pattern reg = Pattern.compile("https?://(www\\.)?steamcommunity\\.com/workshop/filedetails/\\?id=([0-9]+).*");
                for (Element link : doc.getElementById("RequiredItems").getElementsByTag("a")) {
                    if (link.hasAttr("href")) {
                        int lId = match(reg.matcher(link.attr("href")));
                        if (lId > 0) {
                            todo.add(new RemoteModParser(lId));
                            mod.getOverwrite().add(getOrCreateModForId(lId, manager));
                        }
                    }
                }
                System.out.println("Added required items for remote id " + id);
            }
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    private int match(Matcher matcher) {
        try {
            if (matcher.find()) {
                String result = matcher.group(2);
                return Integer.parseInt(result, 10);
            }
        } catch (NumberFormatException e) {
            System.out.println("failed matching number in link: "+e.getMessage());
        }
        return -1;
    }

    @Override
    public String getIdentifier() {
        return String.valueOf(id);
    }

    @Override
    public List<ProcessTask> handle(EntityManager manager) {
        if (!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        Modification mod = getOrCreateModForId(id, manager);
        if (null == mod.getDescription() || "".equals(mod.getDescription())) {//not already processed
            fill(mod, manager);
        }
        manager.getTransaction().commit();
        return todo;
    }
    private Modification getOrCreateModForId(int mid, EntityManager manager) {
        Modification mod;
        try {
            mod = (Modification) manager.createNamedQuery("modifications.id", Modification.class)
                .setParameter("id", mid)
                .getSingleResult();
        } catch(NoResultException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Creating new mod for remote id " + mid);
            mod = new Modification("", mid);
            mod.setCollides(new Colliding(mod));
            manager.persist(mod);
        }
        return mod;
    }
}
