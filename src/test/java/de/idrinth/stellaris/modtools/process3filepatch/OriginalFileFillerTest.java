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
package de.idrinth.stellaris.modtools.process3filepatch;

import de.idrinth.stellaris.modtools.abstract_cases.TestATask;
import de.idrinth.stellaris.modtools.filesystem.FileSystemLocation;
import de.idrinth.stellaris.modtools.persistence.PersistenceProvider;
import de.idrinth.stellaris.modtools.persistence.entity.Original;
import de.idrinth.stellaris.modtools.process.ProcessTask;
import java.io.File;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.io.FileUtils;
import org.h2.util.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class OriginalFileFillerTest extends TestATask {

    @Override
    protected ProcessTask get() {
        return get(1);
    }
    protected ProcessTask get(long id) {
        return new OriginalFileFiller(id, new FileSystemLocationImpl(getAllowedFolder()));
    }
    /**
     * Test of handle method, of class Task.
     * @throws java.lang.Exception
     * @deprecated has to be implemented on a case by case basis
     */
    @Test
    public void testHandle() throws Exception {
        System.out.println("handle");
        EntityManager manager = new PersistenceProvider().get();
        manager.getTransaction().begin();
        IOUtils.copyAndClose(
            getClass().getResourceAsStream("/test.txt"),
            FileUtils.openOutputStream(new File(getAllowedFolder()+"/steamapps/common/Stellaris/test.txt"))
        );
        Original original = new Original("test.txt");
        manager.persist(original);
        manager.getTransaction().commit();
        List<ProcessTask> result = get(original.getAid()).handle(manager);
        Assert.assertEquals(
            "Follow-up number is wrong",
            1,
            result.size()
        );
        manager.getTransaction().begin();
        manager.refresh(original);
        Assert.assertTrue(
            "Content was not written",
            original.getContent().length() > 0
        );
        manager.getTransaction().commit();
    }
    private class FileSystemLocationImpl implements FileSystemLocation {
        private final File folder;

        public FileSystemLocationImpl(File folder) {
            this.folder = folder;
        }

        @Override
        public File get() {
            return folder;
        }
    }
}