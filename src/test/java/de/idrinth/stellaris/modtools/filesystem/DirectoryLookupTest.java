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

import de.idrinth.stellaris.modtools.filesystem.DirectoryLookup;
import org.junit.Assert;
import org.junit.Test;

public class DirectoryLookupTest {

    /**
     * Test of getModDir method, of class DirectoryLookup.
     */
    @Test
    public void testGetModDir() throws Exception {
        System.out.println("getModDir");
        Assert.assertTrue("Mod Directory was not found to exists.", DirectoryLookup.getModDir().exists());
    }

    /**
     * Test of getSteamDir method, of class DirectoryLookup.
     */
    @Test
    public void testGetSteamDir() throws Exception {
        System.out.println("getSteamDir");
        Assert.assertTrue("Steam Directory was not found to exists.", DirectoryLookup.getSteamDir().exists());
    }
    
}
