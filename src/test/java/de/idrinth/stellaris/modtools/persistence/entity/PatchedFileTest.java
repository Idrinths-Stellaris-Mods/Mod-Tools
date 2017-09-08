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
import junit.framework.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class PatchedFileTest extends TestAnyEntity {
    /**
     * Test of isPatchable+setPatchable method, of class PatchedFile.
     */
    @Test
    public void testPatchable() {
        System.out.println("patchable");
        PatchedFile instance = new PatchedFile();
        instance.setPatchable(true);
        Assert.assertTrue("Patchable wasn't set correctly", instance.isPatchable());
        instance.setPatchable(false);
        Assert.assertFalse("Patchable wasn't set correctly", instance.isPatchable());
    }

    /**
     * Test of isPatchableExt+setPatchableExt method, of class PatchedFile.
     */
    @Test
    public void testPatchableExt() {
        System.out.println("patchableExt");
        PatchedFile instance = new PatchedFile();
        instance.setPatchableExt(true);
        Assert.assertTrue("PatchableExt wasn't set correctly", instance.isPatchableExt());
        instance.setPatchableExt(false);
        Assert.assertFalse("PatchableExt wasn't set correctly", instance.isPatchableExt());
    }

    /**
     * Test of getOriginal+setOriginal method, of class PatchedFile.
     */
    @Test
    public void testOriginal() {
        System.out.println("original");
        PatchedFile instance = new PatchedFile();
        Original original = new Original();
        Assert.assertNull("original was pre-set?", instance.getOriginal());
        instance.setOriginal(original);
        Assert.assertTrue("original was not set or not retrieved", original.equals(instance.getOriginal()));
    }

    /**
     * Test of getContent+setContent method, of class PatchedFile.
     */
    @Test
    public void testContent() {
        System.out.println("content");
        PatchedFile instance = new PatchedFile();
        instance.setContent("test 1");
        assertEquals("Content couldn't be set", "test 1" ,instance.getContent());
        instance.setContent("test 12");
        assertEquals("Content couldn't be updated", "test 12" ,instance.getContent());
    }

    /**
     * Test of getModifications+setModifications method, of class PatchedFile.
     */
    @Test
    public void testModifications() {
        System.out.println("modifications");
        PatchedFile instance = new PatchedFile();
        Set<Modification> set = new HashSet<>();
        set.add(new Modification());
        Assert.assertNotNull("there was no modification set pre-set", instance.getModifications());
        Assert.assertFalse("modification sets were equal?", set.equals(instance.getModifications()));
        instance.setModifications(set);
        Assert.assertTrue("modification sets were inequal", set.equals(instance.getModifications()));
    }

    /**
     * Test of getImportance+setImportance method, of class PatchedFile.
     */
    @Test
    public void testImportance() {
        System.out.println("importance");
        PatchedFile instance = new PatchedFile();
        instance.setImportance(1);
        assertEquals("Importance couldn't be set", 1, instance.getImportance());
        instance.setImportance(2);
        assertEquals("Importance couldn't be overwritten", 2, instance.getImportance());
    }

    @Override
    protected BaseEntity get() {
        return new PatchedFile();
    }
}
