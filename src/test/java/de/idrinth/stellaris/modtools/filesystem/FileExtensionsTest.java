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
package de.idrinth.stellaris.modtools.filesystem;

import de.idrinth.stellaris.modtools.filesystem.FileExtensions;
import org.junit.Assert;
import org.junit.Test;

public class FileExtensionsTest {

    /**
     * Test of isPatchable method, of class FileExtensions.
     */
    @Test
    public void testIsPatchable() {
        System.out.println("isPatchable");
        Assert.assertTrue(FileExtensions.isPatchable("demo.txt"));
        Assert.assertFalse(FileExtensions.isPatchable("demo.no-text"));
    }

    /**
     * Test of isReplaceable method, of class FileExtensions.
     */
    @Test
    public void testIsReplaceable() {
        System.out.println("isReplaceable");
        Assert.assertTrue(FileExtensions.isReplaceable("demo.ogg"));
        Assert.assertFalse(FileExtensions.isReplaceable("demo.ogg-text"));
    }

    /**
     * Test of getPatchable method, of class FileExtensions.
     */
    @Test
    public void testGetPatchable() {
        System.out.println("getPatchable");
        Assert.assertTrue(FileExtensions.getPatchable().length > 0);
    }

    /**
     * Test of getReplaceable method, of class FileExtensions.
     */
    @Test
    public void testGetReplaceable() {
        System.out.println("getReplaceable");
        Assert.assertTrue(FileExtensions.getReplaceable().length > 0);
    }
    
}
