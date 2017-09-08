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
package de.idrinth.stellaris.modtools.process2prepatchcleaning;

import de.idrinth.stellaris.modtools.abstract_cases.TestATask;
import de.idrinth.stellaris.modtools.persistence.entity.Modification;
import de.idrinth.stellaris.modtools.persistence.entity.Original;
import de.idrinth.stellaris.modtools.persistence.entity.Patch;
import de.idrinth.stellaris.modtools.process.ProcessTask;
import de.idrinth.stellaris.modtools.persistence.PersistenceProvider;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.Assert;
import org.junit.Test;

public class RemoveSingleUseFilesTest extends TestATask {

    @Override
    protected ProcessTask get() {
        return new RemoveSingleUseFiles(1);
    }

    /**
     * Test of handle method, of class Task.
     */
    @Test
    public void testHandle() {
        System.out.println("basics");
        EntityManager manager = new PersistenceProvider().get();
        try {
            manager.getTransaction().begin();
            // creating data
            Modification mod = new Modification("",1);
            mod.setName("Test 1");
            manager.persist(mod);
            Original original = new Original("commons/test");
            manager.persist(original);
            manager.persist(new Patch(mod, original));
            // storing data
            manager.getTransaction().commit();
            //testing
            List<?> result = new RemoveSingleUseFiles(original.getAid()).handle(manager);
            Assert.assertTrue(
                "handle didn't run properly",
                result instanceof List<?>
            );
            Assert.assertEquals(
                "handle didn't run properly",
                0,
                result.size()
            );
            Assert.assertNull(
                "Patch+File wasn't removed correctly",
                manager.find(Original.class, original.getAid())
            );
        } catch(Exception e) {
            manager.getTransaction().rollback();
            Assert.fail(e.getMessage());
        }
    }
}