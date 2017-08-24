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

import javafx.fxml.Initializable;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author bbuettner
 */
public class FXMLControllerTest {
    
    public FXMLControllerTest() {
    }

    /**
     * Test of constructor of class FXMLController.
     */
    @Test
    public void testFXMLController() {
        System.out.println("construct");
        Assert.assertNotNull("FXMLController can't be constructed", new FXMLController());
    }

    /**
     * Test of initialize method, of class FXMLController.
     */
    @Test
    public void testInitialize() {
        System.out.println("initialize");
        FXMLController instance = new FXMLController();
        instance.initialize(null, null);
        Assert.assertTrue("FXMLController can't be initialized",Initializable.class.isAssignableFrom(instance.getClass()));
    }
    
}
