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

import de.idrinth.stellaris.modtools.entity.Patch;
import de.idrinth.stellaris.modtools.abstract_cases.TestATask;
import de.idrinth.stellaris.modtools.entity.Modification;
import de.idrinth.stellaris.modtools.entity.Original;
import de.idrinth.stellaris.modtools.process.ProcessTask;
import de.idrinth.stellaris.modtools.service.PersistenceProvider;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.Assert;
import org.junit.Test;

public class RemoveOverwrittenFilePatchTest extends TestATask {

    @Override
    protected ProcessTask get() {
        return new RemoveOverwrittenFilePatch(1);
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
            Modification mod1 = new Modification("",1);
            Modification mod2 = new Modification("",2);
            mod1.setName("Test 1");
            mod2.setName("Test 2");
            manager.persist(mod1);
            manager.persist(mod2);
            mod2.getOverwrite().add(mod1);
            Original original = new Original("commons/test");
            manager.persist(original);
            Patch patch1 = new Patch(mod1, original);
            mod1.getPatches().add(patch1);
            original.getPatches().add(patch1);
            manager.persist(patch1);
            Patch patch2 = new Patch(mod2, original);
            mod2.getPatches().add(patch2);
            original.getPatches().add(patch2);
            manager.persist(patch2);
            // storing data
            manager.getTransaction().commit();
            //testing
            Assert.assertTrue(
                "handle didn't run properly",
                new RemoveOverwrittenFilePatch(original.getAid()).handle(manager) instanceof List<?>
            );
            Assert.assertEquals(
                "Patch wasn't removed correctly",
                1,
                manager.find(Original.class, original.getAid()).getPatches().size()
            );
        } catch(Exception e) {
            manager.getTransaction().rollback();
            Assert.fail(e.getMessage());
        }
    }
}
