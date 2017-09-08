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
import junit.framework.Assert;
import org.junit.Test;

public class LazyTextTest extends TestAnyEntity {

    /**
     * Test of setText+getText method, of class LazyText.
     */
    @Test
    public void testText() {
        System.out.println("set/getText");
        LazyText instance = (LazyText) get();
        Assert.assertEquals("entity did not contain expected text", "example", instance.getText());
        instance.setText("example 3");
        Assert.assertEquals("entity did not contain expected text", "example 3", instance.getText());
    }

    /**
     * Test of toString method, of class LazyText.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Assert.assertEquals("entity did not contain expected text", "example", get().toString());
    }

    @Override
    protected BaseEntity get() {
        return new LazyText("example");
    }
    
}
