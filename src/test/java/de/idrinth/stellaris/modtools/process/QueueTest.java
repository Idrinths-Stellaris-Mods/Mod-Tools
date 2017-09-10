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
import de.idrinth.stellaris.modtools.persistence.PersistenceProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import javax.persistence.EntityManager;
import junit.framework.Assert;
import org.junit.Test;

public class QueueTest {

    /**
     * Test of add method, of class Queue.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        TestProcessTask task = new TestProcessTask();
        Queue instance = new Queue(new TestDataInitializer(new TestProcessTask()),new TestCallable(),new TestProgressElementGroup(),"", new PersistenceProvider());
        instance.add(task);
        instance.run();
        Assert.assertTrue("task was not added to run",task.done);
    }

    /**
     * Test of run method, of class Queue.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        TestProcessTask task = new TestProcessTask();
        Queue instance = new Queue(new TestDataInitializer(task),new TestCallable(),new TestProgressElementGroup(),"", new PersistenceProvider());
        instance.run();
        Assert.assertTrue("task was not run",task.done);
    }

    private class TestCallable implements Callable {

        @Override
        public Object call() {
            return null;
        }
        
    }
    private class TestDataInitializer implements DataInitializer {
        private boolean wasPulled = false;
        private final ProcessTask task;

        public TestDataInitializer(ProcessTask task) {
            this.task = task;
        }
        
        @Override
        public ProcessTask poll() {
            wasPulled = true;
            return task;
        }

        @Override
        public boolean hasNext() {
            return !wasPulled;
        }

        @Override
        public int getQueueSize() {
            return 1;
        }
        
    }
    private class TestProcessTask implements ProcessTask {
        public volatile boolean done=false;
        
        @Override
        public List<ProcessTask> handle(EntityManager manager) {
            done=true;
            return new ArrayList<>();
        }

        @Override
        public String getIdentifier() {
            return "demo";
        }
    }
    private class TestProgressElementGroup implements ProgressElementGroup {

        @Override
        public void addToStepLabels(String text) {
            //not of interest
        }

        @Override
        public void update(int current, int maximum) {
            //not of interest
        }
        
    }
}
