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
package de.idrinth.stellaris.modtools.filter;

import java.io.FilenameFilter;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author bbuettner
 */
public class FileExtTest {

    /**
     * Test of accept method, of class FileExt.
     */
    @Test
    public void testAccept() {
        System.out.println("accept");
        FileExt instance = new FileExt("example");
        assertTrue("my.example doesn't seem to end with .example", instance.accept(null, "my.example"));
        assertFalse("my.example.not does seem to end with .example", instance.accept(null, "my.example.not"));
        FileExt dottedInstance = new FileExt(".example");
        assertTrue("my.example doesn't seem to end with .example", dottedInstance.accept(null, "my.example"));
        assertFalse("my.example.not does seem to end with .example", dottedInstance.accept(null, "my.example.not"));
    }

    /**
     * Test of accept method, of class FileExt.
     */
    @Test
    public void testFileExtTest() {
        System.out.println("constructor");
        assertTrue("FileExt is a FilenameFilter",FilenameFilter.class.isAssignableFrom(FileExt.class));
        assertNotNull("FileExt can be constructed",new FileExt("a"));
    }
}
