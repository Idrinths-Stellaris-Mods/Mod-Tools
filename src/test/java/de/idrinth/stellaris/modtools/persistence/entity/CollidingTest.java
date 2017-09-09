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
import org.junit.Assert;
import org.junit.Test;

public class CollidingTest extends TestAnyEntity {

    /**
     * Test of setModification+getModification method, of class Colliding.
     */
    @Test
    public void testModification() {
        System.out.println("modification");
        Modification mod = new Modification("abc",123);
        Colliding instance = (Colliding) get();
        instance.setModification(mod);
        Assert.assertTrue("Mod couldn't be set and retrieved correctly",mod.equals(instance.getModification()));
    }

    /**
     * Test of setModifications method, of class Colliding.
     */
    @Test
    public void testSetModifications() {
        System.out.println("modifications");
        Colliding instance = (Colliding) get();
        Set<Modification> set = new HashSet<>();
        set.add(new Modification());
        junit.framework.Assert.assertNotNull("there was no modification set pre-set", instance.getModifications());
        junit.framework.Assert.assertFalse("modification sets were equal?", set.equals(instance.getModifications()));
        instance.setModifications(set);
        junit.framework.Assert.assertTrue("modification sets were inequal", set.equals(instance.getModifications()));
    }

    @Override
    protected BaseEntity get() {
        return new Colliding();
    }
    
}
