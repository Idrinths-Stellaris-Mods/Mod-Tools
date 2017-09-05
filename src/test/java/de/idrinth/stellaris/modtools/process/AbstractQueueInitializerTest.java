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

import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractQueueInitializerTest {
    /**
     * Test of poll method, of class AbstractQueueInitializer.
     */
    @Test
    public void testPoll() {
        System.out.println("poll");
        DataInitializer instance = new AbstractQueueInitializerImpl();
        assertEquals(null, instance.poll());
    }

    /**
     * Test of hasNext method, of class AbstractQueueInitializer.
     */
    @Test
    public void testHasNext() {
        System.out.println("hasNext");
        DataInitializer instance = new AbstractQueueInitializerImpl();
        assertEquals(false, instance.hasNext());
    }

    /**
     * Test of getQueueSize method, of class AbstractQueueInitializer.
     */
    @Test
    public void testGetQueueSize() {
        System.out.println("getQueueSize");
        DataInitializer instance = new AbstractQueueInitializerImpl();
        assertEquals(20, instance.getQueueSize());
    }

    /**
     * Test of initOnce method, of class AbstractQueueInitializer.
     */
    @Test
    public void testInitOnce() {
        System.out.println("getQueueSize");
        AbstractQueueInitializerImpl instance = new AbstractQueueInitializerImpl();
        assertEquals(0, instance.initCalledCount);
        instance.hasNext();
        assertEquals(1, instance.initCalledCount);
        instance.hasNext();
        assertEquals(1, instance.initCalledCount);
    }

    public class AbstractQueueInitializerImpl extends AbstractQueueInitializer {
        public volatile int initCalledCount=0;
        @Override
        public void init() {
            initCalledCount++;
        }
    }
    
}
