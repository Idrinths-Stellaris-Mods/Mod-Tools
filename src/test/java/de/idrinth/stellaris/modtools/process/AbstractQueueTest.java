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

import de.idrinth.stellaris.modtools.gui.ProgressElementGroup;
import java.util.concurrent.Callable;
import junit.framework.Assert;
import org.junit.Test;

public class AbstractQueueTest {
    
    public AbstractQueueTest() {
    }

    /**
     * Test of add method, of class AbstractQueue.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        AbstractQueueImpl queue = new AbstractQueueImpl();
        queue.addList();
        queue.add(new TestRunnable(queue));
        queue.add(new TestRunnable(queue,"ääää"));
        queue.run();
        Assert.assertEquals(2, queue.increment);
    }

    /**
     * Test of run method, of class AbstractQueue.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        AbstractQueueImpl queue = new AbstractQueueImpl();
        queue.addList();
        queue.run();
        Assert.assertEquals(1, queue.increment);
    }

    private class AbstractQueueImpl extends AbstractQueue {
        public volatile int increment=0;
        public AbstractQueueImpl() {
            super(new TestCallable(), new TestProgressElementGroup(), "");
        }

        @Override
        public void addList() {
            add(new TestRunnable(this));
        }
    }
    private class TestProgressElementGroup implements ProgressElementGroup {

        @Override
        public void addToStepLabels(String text) {
            //done
        }

        @Override
        public void update(int current, int maximum) {
            //done
        }
        
    }
    private class TestCallable implements Callable {
        @Override
        public Object call() {
            return null;//nothing to do;
        }
    }
    private class TestRunnable extends Task {
        private final String identifier;
        public TestRunnable(AbstractQueueImpl queue, String identifier) {
            super(queue);
            this.identifier = identifier;
        }
        public TestRunnable(AbstractQueueImpl queue) {
            this(queue, "-");
        }
        @Override
        public void run() {
            fill();
        }
        @Override
        protected void fill() {
            ((AbstractQueueImpl) queue).increment++;
        }

        @Override
        protected String getIdentifier() {
            return identifier;
        }
    }
}
