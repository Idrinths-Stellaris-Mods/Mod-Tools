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
package de.idrinth.stellaris.modtools.process1datacollection;

import java.io.File;
import junit.framework.Assert;
import org.junit.Test;

public class FileExtFilterTest {
    /**
     * Test of accept method, of class FileExtFilter.
     */
    @Test
    public void testAccept() {
        System.out.println("accept");
        FileExtFilter ff = new FileExtFilter("txt");
        Assert.assertTrue("demo.txt is not ending with.txt?", ff.accept(new File("./"), "demo.txt"));
        Assert.assertFalse("demo.täxt is ending with.txt?", ff.accept(new File("./"), "demo.täxt"));
        Assert.assertFalse("demo.txt.aa is ending with.txt?", ff.accept(new File("./"), "demo.txt.aa"));
    }
    
}
