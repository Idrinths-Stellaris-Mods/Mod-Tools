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

public class VersionTest {

    /**
     * Test of addIfBigger method, of class Version.
     */
    @Test
    public void testAddIfBigger() {
        System.out.println("addIfBigger");
        String v1 = "1.0.7";
        String v2 = "1.0.2";
        String v3 = "1.0.*";
        String v4 = "1.1";
        Version instance = new Version();
        instance.addIfBigger(v1);
        Assert.assertEquals("1.0.7 is not more than 1.0.0?",v1,instance.toString());
        instance.addIfBigger(v2);
        Assert.assertEquals("1.0.2 is more than 1.0.7?",v1,instance.toString());
        instance.addIfBigger(v3);
        Assert.assertEquals("1.0.* is not more than 1.0.7?",v3,instance.toString());
        instance.addIfBigger(v4);
        Assert.assertEquals("1.1 is not more than 1.0.*?",v4+".0",instance.toString());
    }

    /**
     * Test of toString method, of class Version.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Assert.assertEquals("1.0.0", new Version().toString());
    }
    
}
