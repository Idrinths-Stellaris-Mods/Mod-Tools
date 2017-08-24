/*
 * Copyright (C) 2017 Idrinth
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

import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bbuettner
 */
public class MainAppTest {
    
    public MainAppTest() {
    }

    /**
     * Test of constructor of class MainApp.
     */
    @Test
    public void testMainApp() {
        System.out.println("constructor");
        Assert.assertTrue("MainApp is an Application",Application.class.isAssignableFrom(MainApp.class));
        Assert.assertNotNull("MainApp can be constructed",new MainApp());
    }
    
}
