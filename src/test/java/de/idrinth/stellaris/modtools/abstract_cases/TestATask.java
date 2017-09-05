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

import de.idrinth.stellaris.modtools.process.ProcessTask;
import org.junit.Assert;
import org.junit.Test;

public abstract class TestATask {
    
    protected abstract ProcessTask get();

    @Test
    public void testInterface() {
        System.out.println("interface");
        Assert.assertTrue("This task does not implement the required interface.", ProcessTask.class.isAssignableFrom(get().getClass()));
    }

    /**
     * Test of getFullIdentifier method, of class Task.
     */
    @Test
    public void testGetFullIdentifier() {
        System.out.println("getIdentifier");
        Assert.assertTrue("Full Identifier is not correct", get().getIdentifier().length() > 0);
    }
    
}
