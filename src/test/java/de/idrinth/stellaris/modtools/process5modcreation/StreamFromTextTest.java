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
package de.idrinth.stellaris.modtools.process5modcreation;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class StreamFromTextTest {

    /**
     * Test of get method, of class StreamFromText.
     * @throws java.io.IOException
     */
    @Test
    public void testGet() throws IOException {
        System.out.println("get");
        StreamFromText instance = new StreamFromText("This is an example");
        assertEquals("This is an example", IOUtils.toString(instance.get(),"utf-8"));
    }
    
}
