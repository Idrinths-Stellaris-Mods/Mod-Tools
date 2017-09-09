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
package de.idrinth.stellaris.modtools.persistence.entity;

import de.idrinth.stellaris.modtools.abstract_cases.TestAnyEntity;
import junit.framework.Assert;
import org.junit.Test;

public class PatchTest extends TestAnyEntity {

    /**
     * Test of getMod+setMod method, of class Patch.
     */
    @Test
    public void testMod() {
        System.out.println("mod");
        Patch instance = new Patch();
        Modification mod = new Modification("abc",123);
        instance.setMod(mod);
        Assert.assertTrue("Mod couldn't be set and retrieved correctly",mod.equals(instance.getMod()));
    }

    /**
     * Test of getFile+setFile method, of class Patch.
     */
    @Test
    public void testFile() {
        System.out.println("file");
        Patch instance = new Patch();
        Original original = new Original();
        Assert.assertNull("original was pre-set?", instance.getFile());
        instance.setFile(original);
        Assert.assertTrue("original was not set or not retrieved", original.equals(instance.getFile()));
    }

    /**
     * Test of getDiff+setDiff method, of class Patch.
     */
    @Test
    public void testDiff() {
        System.out.println("diff");
        Patch instance = new Patch();
        instance.setDiff("test 1");
        Assert.assertEquals("Content couldn't be set", "test 1" ,instance.getDiff());
        instance.setDiff("test 12");
        Assert.assertEquals("Content couldn't be updated", "test 12" ,instance.getDiff());
    }

    @Override
    protected BaseEntity get() {
        return new Patch();
    }
}
