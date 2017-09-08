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
package de.idrinth.stellaris.modtools.persistence.entity;

import de.idrinth.stellaris.modtools.abstract_cases.TestAnyEntity;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class ModificationTest extends TestAnyEntity {

    /**
     * Test of getCollides+setCollides method, of class Modification.
     */
    @Test
    public void testCollides() {
        System.out.println("collides");
        Colliding collides = new Colliding();
        collides.setAid(111);
        Modification instance = new Modification();
        Assert.assertNotNull("Collides aren't preset?",instance.getCollides());
        Assert.assertNotEquals("Different Collides match?",collides,instance.getCollides());
        instance.setCollides(collides);
        Assert.assertEquals("Collides can't be replaced?",collides,instance.getCollides());
    }

    /**
     * Test of getName+setName method, of class Modification.
     */
    @Test
    public void testName() {
        System.out.println("name");
        Modification instance = (Modification) get();
        instance.setName("abc");
        Assert.assertEquals("Name wasn't set properly by setter", "abc", instance.getName());
        instance.setName("testing");
        Assert.assertEquals("Name wasn't changed properly by setter", "testing", instance.getName());
    }

    /**
     * Test of getConfigPath+setConfigPath method, of class Modification.
     */
    @Test
    public void testConfigPath() {
        System.out.println("configPath");
        Modification instance = (Modification) get();
        Assert.assertEquals("config path wasn't set properly by constructor", "1.mod", instance.getConfigPath());
        instance.setConfigPath("testing");
        Assert.assertEquals("config path wasn't changed properly by setter", "testing", instance.getConfigPath());
    }

    /**
     * Test of getVersion+setVersion method, of class Modification.
     */
    @Test
    public void testVersion() {
        System.out.println("version");
        Modification instance = (Modification) get();
        instance.setVersion("abc");
        Assert.assertEquals("Version wasn't set properly by setter", "abc", instance.getVersion());
        instance.setVersion("testing");
        Assert.assertEquals("Version wasn't changed properly by setter", "testing", instance.getVersion());
    }

    /**
     * Test of getId+setId method, of class Modification.
     */
    @Test
    public void testId() {
        System.out.println("id");
        Modification instance = (Modification) get();
        Assert.assertEquals("id wasn't set properly by constructor", 1, instance.getId());
        instance.setId(2);
        Assert.assertEquals("id wasn't changed properly by setter", 2, instance.getId());
    }

    /**
     * Test of getOverwrite+setOverwrite method, of class Modification.
     */
    @Test
    public void testOverwrite() {
        System.out.println("overwrite");
        Modification instance = (Modification) get();
        Set<Modification> set = new HashSet<>();
        set.add(new Modification());
        Assert.assertNotNull("there was no modification set pre-set", instance.getOverwrite());
        Assert.assertFalse("modification sets were equal?", set.equals(instance.getOverwrite()));
        instance.setOverwrite(set);
        Assert.assertTrue("modification sets were inequal", set.equals(instance.getOverwrite()));
    }

    /**
     * Test of getDescription+setDescription method, of class Modification.
     */
    @Test
    public void testDescription() {
        System.out.println("description");
        Modification instance = new Modification();
        instance.setDescription("test 1");
        assertEquals("Description couldn't be set", "test 1" ,instance.getDescription());
        instance.setDescription("test 12");
        assertEquals("Description couldn't be updated", "test 12" ,instance.getDescription());
    }

    /**
     * Test of getPatches+setPatches method, of class Modification.
     */
    @Test
    public void testPatches() {
        System.out.println("patches");
        Modification instance = new Modification();
        Set<Patch> set = new HashSet<>();
        set.add(new Patch());
        Assert.assertNotNull("there was no patch set pre-set", instance.getPatches());
        Assert.assertFalse("patch sets were equal?", set.equals(instance.getPatches()));
        instance.setPatches(set);
        Assert.assertTrue("patch sets were inequal", set.equals(instance.getPatches()));
    }

    @Override
    protected BaseEntity get() {
        return new Modification("1.mod",1);
    }
    
}
