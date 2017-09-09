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
import java.util.HashSet;
import java.util.Set;
import junit.framework.Assert;
import org.junit.Test;

public class OriginalTest extends TestAnyEntity {

    /**
     * Test of getContent+setContent method, of class Original.
     */
    @Test
    public void testContent() {
        System.out.println("content");
        Original instance = new Original();
        instance.setContent("test 1");
        Assert.assertEquals("Content couldn't be set", "test 1" ,instance.getContent());
        instance.setContent("test 12");
        Assert.assertEquals("Content couldn't be updated", "test 12" ,instance.getContent());
    }

    /**
     * Test of getRelativePath+setRelativePath method, of class Original.
     */
    @Test
    public void testRelativePath() {
        System.out.println("relativePath");
        Original instance = new Original("aaaa");
        Assert.assertEquals("Path was not correctly set by constructor", "aaaa", instance.getRelativePath());
        instance.setRelativePath("baaaa");
        Assert.assertEquals("Path was not correctly set by setter", "baaaa", instance.getRelativePath());
    }

    /**
     * Test of getPatches+setPatched method, of class Original.
     */
    @Test
    public void testPatches() {
        System.out.println("patches");
        Original instance = new Original();
        Set<Patch> set = new HashSet<>();
        set.add(new Patch());
        Assert.assertNotNull("there was no patch set pre-set", instance.getPatches());
        Assert.assertFalse("patch sets were equal?", set.equals(instance.getPatches()));
        instance.setPatches(set);
        Assert.assertTrue("patch sets were inequal", set.equals(instance.getPatches()));
    }

    @Override
    protected BaseEntity get() {
        return new Original("demo");
    }
}
