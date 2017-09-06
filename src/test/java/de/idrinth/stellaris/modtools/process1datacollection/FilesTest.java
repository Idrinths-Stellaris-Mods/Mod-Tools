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
import de.idrinth.stellaris.modtools.persistence.entity.Patch;
import de.idrinth.stellaris.modtools.process.ProcessTask;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.Assert;
import org.junit.Test;

public class FilesTest extends TestATask {

    @Override
    protected ProcessTask get() {
        return new FilesImpl("n");
    }

    /**
     * Test of addToFiles method, of class Files.
     */
    @Test
    public void testAddToFiles() {
        System.out.println("addToFiles");
        EntityManager manager = new PersistenceProvider().get();
        manager.getTransaction().begin();
        manager.persist(new Modification("files.mod",1000));
        manager.getTransaction().commit();
        new FilesImpl("files.mod").addFile("testfile", "testcontent", manager);
        Assert.assertFalse(
            "No patch was written",
            manager.createNamedQuery("patch.any", Patch.class).getResultList().isEmpty()
        );
        Assert.assertEquals(
            "Wrong number of pathes avaible",
            1,
            manager.createNamedQuery("patch.any", Patch.class).getResultList().size()
        );
        Assert.assertEquals(
            "Content was saved incorrectly",
            "testcontent",
            manager.createNamedQuery("patch.any", Patch.class).getSingleResult().getDiff()
        );
    }

    private class FilesImpl extends Files {

        public FilesImpl(String modConfigName) {
            super(modConfigName);
        }
        public void addFile(String name, String content, EntityManager manager) {
            this.addToFiles(name, content, manager);
        }

        @Override
        public List<ProcessTask> handle(EntityManager manager) {
            return new ArrayList<>();
        }
    }
}
