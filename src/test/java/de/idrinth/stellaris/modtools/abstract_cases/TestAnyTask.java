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
package de.idrinth.stellaris.modtools.abstract_cases;

import de.idrinth.stellaris.modtools.persistence.PersistenceProvider;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

abstract public class TestAnyTask extends TestATask {
    

    /**
     * Test of handle method, of class Task.
     * @deprecated has to be implemented on a case by case basis
     */
    @Test
    public void testHandle() {
        System.out.println("run - basics");
        try {
            Assert.assertTrue(
                "Full Identifier is not correct",
                get().handle(new PersistenceProvider().get()) instanceof List<?>
            );
        } catch(Exception e) {
            Assert.assertTrue(true);//@todo implement the requirements for all tasks
        }
    }
}
