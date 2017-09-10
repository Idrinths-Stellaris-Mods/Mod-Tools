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

import de.idrinth.stellaris.modtools.persistence.PersistenceProvider;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import junit.framework.Assert;
import org.junit.Test;

public class TaskTest {
    /**
     * Test of run method, of class Task.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        CounterQueue queue = new CounterQueue();
        Task instance = new Task(queue, new CounterProcessTask(0), new PersistenceProvider());
        instance.run();
        Assert.assertEquals("Not all results reached Queue", 3, queue.count);
    }

    /**
     * Test of getFullIdentifier method, of class Task.
     */
    @Test
    public void testGetFullIdentifier() {
        System.out.println("getFullIdentifier");
        Assert.assertEquals(
            CounterProcessTask.class.getName()+"@7",
            new Task(null, new CounterProcessTask(7), new PersistenceProvider()).getFullIdentifier()
        );
    }
    private class CounterProcessTask implements ProcessTask {
        private final int id;

        public CounterProcessTask(int id) {
            this.id = id;
        }
        
        @Override
        public List<ProcessTask> handle(EntityManager manager) {
            ArrayList<ProcessTask> list = new ArrayList<>();
            list.add(new CounterProcessTask(1));
            list.add(new CounterProcessTask(2));
            list.add(new CounterProcessTask(3));
            return list;
        }

        @Override
        public String getIdentifier() {
            return String.valueOf(id);
        }
        
    }
    private class CounterQueue implements ProcessHandlingQueue {
        public volatile int count=0;
        @Override
        public void add(ProcessTask task) {
            count++;
        }

        @Override
        public void run() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
}
