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

import org.junit.Assert;
import org.junit.Test;

public class DirectoryNotFoundExceptionTest {

    @Test
    public void testInitialisation() {
        Exception cause = new Exception("test");
        DirectoryNotFoundException ex = new DirectoryNotFoundException("example-text",cause);
        Assert.assertEquals("Message was not stored correctly", "example-text", ex.getMessage());
        Assert.assertEquals("Cause was not stored correctly", cause, ex.getCause());
    }
    
}
