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
package de.idrinth.stellaris.modtools.abstract_cases;

import de.idrinth.stellaris.modtools.persistence.entity.BaseEntity;
import org.junit.Assert;
import org.junit.Test;

abstract public class TestAnyEntity {
    abstract protected BaseEntity get();
    /**
     * Test of setAid+getAid method.
     */
    @Test
    public void testAid() {
        System.out.println("set/getAid");
        BaseEntity instance = get();
        instance.setAid(7);
        Assert.assertEquals("Setting Aid or getting it failed", 7, instance.getAid());
    }

    /**
     * Test of equals method
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        BaseEntity instance = get();
        instance.setAid(77);
        Assert.assertTrue("is not equal to itself?", instance.equals(instance));
        Assert.assertFalse("Different aids are equal?", instance.equals(get()));
        Assert.assertFalse("Is equal to wrong object", instance.equals(new Object()));
    }

    /**
     * Test of hashCode method
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        BaseEntity instance = get();
        instance.setAid(77);
        Assert.assertNotEquals(
            "has the same hash with different aids?",
            get().hashCode(),
            instance.hashCode()
        );
        Assert.assertEquals(
            "has an unexpected hash",
            instance.getClass().getName().hashCode() * 7 + (int) ((long)77 ^ ((long)77 >>> 32)),
            instance.hashCode()
        );
    }
}