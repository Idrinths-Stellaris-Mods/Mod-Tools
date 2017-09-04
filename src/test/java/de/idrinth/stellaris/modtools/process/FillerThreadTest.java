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
import java.util.ArrayList;
import java.util.concurrent.Callable;
import org.junit.Assert;
import org.junit.Test;

public class FillerThreadTest {

    /**
     * Test of run method, of class FillerThread.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        Assert.assertTrue("Is not a runnable", Runnable.class.isInstance(new FillerThread(new ArrayList<>(), new TestProgressElementGroup())));
    }

    /**
     * Test of call method, of class FillerThread.
     */
    @Test
    public void testCall() throws Exception {
        System.out.println("call");
        Assert.assertTrue("Is not a callable", Callable.class.isInstance(new FillerThread(new ArrayList<>(), new TestProgressElementGroup())));
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
}
