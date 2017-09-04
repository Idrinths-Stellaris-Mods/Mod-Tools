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
package de.idrinth.stellaris.modtools.process;

import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

public class TaskTest {
    
    public TaskTest() {
    }

    /**
     * Test of run method, of class Task.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        Assert.assertTrue(
                "Is not a runnable",
                Runnable.class.isInstance(new TaskImpl().getClass())
        );
    }

    /**
     * Test of getFullIdentifier method, of class Task.
     */
    @Test
    public void testGetFullIdentifier() {
        System.out.println("getFullIdentifier");
        Assert.assertEquals(
                "Full Identifier is not correct",
                TaskImpl.class.getName()+"@abc",
                new TaskImpl().getFullIdentifier()
        );
    }

    public class TaskImpl extends Task {

        public TaskImpl() {
            super(null);
        }

        @Override
        public void fill() throws IOException {
        }

        @Override
        public String getIdentifier() {
            return "abc";
        }
    }
}
