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

import de.idrinth.stellaris.modtools.filesystem.DirectoryNotFoundException;
import de.idrinth.stellaris.modtools.filesystem.FileSystemLocation;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

abstract public class TestAnyFileSystemLocation {
    abstract protected FileSystemLocation makeInstance() throws DirectoryNotFoundException;
    /**
     * Test of get method, of class ModLocation.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        try {
            Assert.assertTrue("Mod Directory was not found", makeInstance().get().exists());
        } catch (DirectoryNotFoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Assert.fail(ex.getMessage());
        }
    }
}
