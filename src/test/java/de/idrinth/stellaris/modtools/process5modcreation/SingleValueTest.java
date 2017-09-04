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
package de.idrinth.stellaris.modtools.process5modcreation;

import junit.framework.Assert;
import org.junit.Test;

public class SingleValueTest {

    /**
     * Test of toString method, of class SingleValue.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        SingleValue instance = new SingleValue();
        instance.addValue("qq");
        Assert.assertEquals(
                "add value does not correctly set value",
                "\"qq\"",
                instance.toString()
        );
    }

    /**
     * Test of addValue method, of class SingleValue.
     */
    @Test
    public void testAddValue() {
        System.out.println("addValue");
        SingleValue instance = new SingleValue();
        instance.addValue("qq");
        instance.addValue("qq22");
        Assert.assertEquals(
                "add value does not correctly overwrite original value",
                "\"qq22\"",
                instance.toString()
        );
    }
}
