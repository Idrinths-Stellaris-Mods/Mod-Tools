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
package de.idrinth.stellaris.modtools.abstractTestCases;

import de.idrinth.stellaris.modtools.process.ProcessHandlingQueue;
import de.idrinth.stellaris.modtools.process.ProcessTask;
import de.idrinth.stellaris.modtools.service.PersistenceProvider;
import javax.persistence.EntityManager;
import org.junit.Assert;
import org.junit.Test;

abstract public class TestAnyTask {
    
    abstract protected ProcessTask get(ProcessHandlingQueue queue);
    protected ProcessTask getWrapped() {
        return get(new UselessQueue());
    }

    @Test
    public void testInterface() {
        System.out.println("interface");
        org.junit.Assert.assertTrue(
            "This task does not implement the required interface.",
            ProcessTask.class.isAssignableFrom(getWrapped().getClass())
        );
    }
    /**
     * Test of run method, of class Task.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        Assert.assertTrue(
            "Is not a runnable",
            Runnable.class.isAssignableFrom(getWrapped().getClass())
        );
    }

    /**
     * Test of getFullIdentifier method, of class Task.
     */
    @Test
    public void testGetFullIdentifier() {
        System.out.println("getFullIdentifier");
        ProcessTask task = getWrapped();
        Assert.assertTrue(
            "Full Identifier is not correct",
            task.getFullIdentifier().startsWith(task.getClass().getName()+"@") &&
            task.getFullIdentifier().length() > task.getClass().getName().length()+1
        );
    }
    private class UselessQueue implements ProcessHandlingQueue {

        @Override
        public void add(ProcessTask task) {
            // not meant to be tested here
        }

        @Override
        public EntityManager getEntityManager() {
            return PersistenceProvider.get();
        }

        @Override
        public void run() {
            // not meant to be tested here
        }
    }
}
