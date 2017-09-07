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
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

public class ZipContentParserTest extends TestATask {

    @Override
    protected ProcessTask get() {
        return new ZipContentParser("ZipContentParserTest.mod", new File(getAllowedFolder()+"/a.zip"));
    }
    /**
     * Test of handle method, of class ZipContentParser.
     */
    @Test
    public void testHandle() {
        System.out.println("handle");
        try {
            EntityManager manager = new PersistenceProvider().get();
            if(!manager.getTransaction().isActive()) {
                manager.getTransaction().begin();
            }
            manager.persist(new Modification("ZipContentParserTest.mod",31));
            manager.getTransaction().commit();
            IOUtils.copy(getClass().getResourceAsStream("/test.zip"), FileUtils.openOutputStream(new File(getAllowedFolder()+"/a.zip")));
            List <ProcessTask> result = get().handle(manager);
            Assert.assertTrue(
                "result is not of correct type",
                result instanceof List<?>
            );
            Assert.assertEquals(
                "Not enough follow-ups",
                1,
                result.size()
            );
        } catch(Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }
}
