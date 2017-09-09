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

import de.idrinth.stellaris.modtools.abstract_cases.TestATask;
import de.idrinth.stellaris.modtools.persistence.PersistenceProvider;
import de.idrinth.stellaris.modtools.persistence.entity.Modification;
import de.idrinth.stellaris.modtools.process.ProcessTask;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class RemoteModParserTest extends TestATask {

    @Override
    protected ProcessTask get() {
        return new NoRemoteModParser();
    }
    /**
     * Test of handle method, of class RemoteModParserTest.
     * @throws java.lang.Exception
     */
    @Test
    public void testHandle() throws Exception {
        System.out.println("handle");
        EntityManager manager = new PersistenceProvider().get();
        List<ProcessTask> result = get().handle(manager);
        Assert.assertEquals(
            "handle does not return correct number of items",
            2,
            result.size()
        );
        Assert.assertEquals(
            "wrong amount of mods created",
            1,
            manager.createNamedQuery("modifications.id", Modification.class)
                .setParameter("id", 123)
                .getResultList()
                .size()
        );
        Modification mod = manager.createNamedQuery("modifications.id", Modification.class).setParameter("id", 123).getSingleResult();
        Assert.assertEquals(
            "Title was not added correctly",
            "!Merge 'Combat Balancing - Naked Corvette Prevention' + 'Ethics, Civics and Traditions Rebuild'",
            mod.getName()
        );
        Assert.assertTrue(
           "Description was not added",
           mod.getDescription().length() > 0
        );
        Assert.assertEquals(
           "Not all overwrites added",
           2,
           mod.getOverwrite().size()
        );
    }
    private class NoRemoteModParser extends RemoteModParser {
        public NoRemoteModParser() {
            super(123);
        }

        @Override
        protected Document getDocument() throws IOException {
            return Jsoup.parse(
                IOUtils.toString(getClass().getResourceAsStream("/steam-mod-website.html"), "utf-8")
            );
        }
    }
}
