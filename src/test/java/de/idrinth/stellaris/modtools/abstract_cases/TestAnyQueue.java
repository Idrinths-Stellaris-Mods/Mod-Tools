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

import de.idrinth.stellaris.modtools.gui.ProgressElementGroup;
import de.idrinth.stellaris.modtools.process.ProcessHandlingQueue;
import de.idrinth.stellaris.modtools.process.ProcessTask;
import java.util.concurrent.Callable;
import javax.persistence.EntityManager;
import junit.framework.Assert;
import org.junit.Test;

abstract public class TestAnyQueue {
    abstract protected ProcessHandlingQueue get(ProgressElementGroup progress,Callable callable);
    protected TestProgressElementGroup getWrapped() {
        TestProgressElementGroup progress = new TestProgressElementGroup();
        progress.queue = get(progress, new TestCallable());
        return progress;
    }

    @Test
    public void testInterface() {
        System.out.println("interface");
        Assert.assertTrue(
            "This queue does not implement the required interface.",
            ProcessHandlingQueue.class.isInstance(getWrapped().queue)
        );
    }
    /**
     * Test of add method, of class AbstractQueue.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        TestProgressElementGroup wrapper = getWrapped();
        wrapper.queue.add(new TestRunnable());
        wrapper.queue.add(new TestRunnable("ääää"));
        wrapper.queue.add(new TestRunnable());
        wrapper.queue.run();
        Assert.assertEquals(2, wrapper.max);
    }

    /**
     * Test of run method, of class AbstractQueue.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        TestProgressElementGroup wrapper = getWrapped();
        wrapper.queue.run();
        Assert.assertEquals(0, wrapper.max);
    }

    /**
     * Test of getEntityManager method, of class AbstractQueue.
     */
    @Test
    public void testGetEntityManager() {
        System.out.println("getEntityManager");
        Assert.assertTrue(
                "getEntityManager does not return an EntityManager",
                EntityManager.class.isAssignableFrom(getWrapped().queue.getEntityManager().getClass())
        );
    }
    protected class TestProgressElementGroup implements ProgressElementGroup {
        public volatile ProcessHandlingQueue queue;
        public volatile int max;
        @Override
        public void addToStepLabels(String text) {
            //done
        }

        @Override
        public void update(int current, int maximum) {
            max = current;
        }
        
    }
    private class TestCallable implements Callable {
        @Override
        public Object call() {
            return null;//nothing to do
        }
    }
    private class TestRunnable implements ProcessTask {
        private final String identifier;
        public TestRunnable(String identifier) {
            this.identifier = identifier;
        }
        public TestRunnable() {
            this.identifier = "-";
        }
        @Override
        public void run() {
            // done, not to be tested here
        }

        @Override
        public String getFullIdentifier() {
            return identifier;
        }
    }
}
