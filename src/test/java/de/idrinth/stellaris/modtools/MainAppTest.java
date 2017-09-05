/*
 * Copyright (C) 2017 Börn Büttner
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
package de.idrinth.stellaris.modtools;

import javafx.stage.Stage;
import junit.framework.Assert;
import org.junit.Test;

public class MainAppTest {

    /**
     * Test of start method, of class MainApp.
     * @throws java.lang.NoSuchMethodException
     */
    @Test
    public void testStart() throws NoSuchMethodException {
        System.out.println("start");
        Assert.assertNotNull("Can't find start method", MainApp.class.getMethod("start",Stage.class));
    }

    /**
     * Test of stop method, of class MainApp.
     * @throws java.lang.NoSuchMethodException
     */
    @Test
    public void testStop() throws NoSuchMethodException {
        System.out.println("stop");
        Assert.assertNotNull("Can't find stop method", MainApp.class.getMethod("stop"));
    }

    /**
     * Test of main method, of class MainApp.
     * @throws java.lang.NoSuchMethodException
     */
    @Test
    public void testMain() throws NoSuchMethodException {
        System.out.println("main");
        Assert.assertNotNull("Can't find main method", MainApp.class.getMethod("main",String[].class));
    }

    /**
     * Test of MainApp method, of class MainApp.
     * @throws java.lang.NoSuchMethodException
     */
    @Test
    public void testInstantiation() throws NoSuchMethodException {
        System.out.println("MainApp");
        Assert.assertNotNull("Can't construct MainApp", new MainApp());
    }
}
