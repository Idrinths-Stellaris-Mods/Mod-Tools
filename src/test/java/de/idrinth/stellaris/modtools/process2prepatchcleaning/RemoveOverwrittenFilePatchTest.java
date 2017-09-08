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

import de.idrinth.stellaris.modtools.persistence.entity.Patch;
import de.idrinth.stellaris.modtools.abstract_cases.TestATask;
import de.idrinth.stellaris.modtools.persistence.entity.Modification;
import de.idrinth.stellaris.modtools.persistence.entity.Original;
import de.idrinth.stellaris.modtools.process.ProcessTask;
import de.idrinth.stellaris.modtools.persistence.PersistenceProvider;
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
            Modification mod3 = new Modification("",3);
            mod1.setName("Test 1");
            mod2.setName("Test 2");
            mod3.setName("Test 3");
            manager.persist(mod1);
            manager.persist(mod2);
            manager.persist(mod3);
            mod2.getOverwrite().add(mod1);
            Original original = new Original("commons/test");
            manager.persist(original);
            manager.persist(new Patch(mod1, original));
            manager.persist(new Patch(mod2, original));
            manager.persist(new Patch(mod3, original));
            // storing data
            manager.getTransaction().commit();
            manager.clear();
            //testing
            List<?> result = new RemoveOverwrittenFilePatch(original.getAid()).handle(manager);
            Assert.assertTrue(
                "handle didn't run properly",
                result instanceof List<?>
            );
            Assert.assertEquals(
                "handle didn't run properly",
                1,
                result.size()
            );
            Assert.assertEquals(
                "Patch wasn't removed correctly",
                2,
                manager.find(Original.class, original.getAid()).getPatches().size()
            );
        } catch(Exception e) {
            manager.getTransaction().rollback();
            Assert.fail(e.getMessage());
        }
    }
}
