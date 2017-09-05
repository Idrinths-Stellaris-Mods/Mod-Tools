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

import de.idrinth.stellaris.modtools.service.DirectoryLookup;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ModTest {
    /**
     * Test of toString method, of class Mod.
     * @throws java.io.IOException
     */
    @Test
    public void testToString() throws IOException {
        System.out.println("toString");
        String expResult = "name = \"Mod\"\n" +
"path = \""+DirectoryLookup.getModDir() + "/file.zip\"\n" +
"dependencies = {\n" +
"}\n" +
"supported_version = \"1.0.*\"\n" +
"tags = {\n" +
"	\"Merge\"\n" +
"}\n";
        assertEquals("Mod differs from expected content",expResult, new Mod("file", "Mod").toString());
    }

    /**
     * Test of addNameValue method, of class Mod.
     * @throws java.io.IOException
     */
    @Test
    public void testAddNameValue() throws IOException {
        System.out.println("addNameValue");
        Mod instance = new Mod("file", "Mod");
        instance.addNameValue("MyMod");
        String expResult = "name = \"MyMod\"\n" +
"path = \""+DirectoryLookup.getModDir() + "/file.zip\"\n" +
"dependencies = {\n" +
"}\n" +
"supported_version = \"1.0.*\"\n" +
"tags = {\n" +
"\t\"Merge\"\n" +
"}\n";
        assertEquals("Mod differs from expected content", expResult, instance.toString());
    }

    /**
     * Test of addVersionValue method, of class Mod.
     * @throws java.io.IOException
     */
    @Test
    public void testAddVersionValue() throws IOException {
        System.out.println("addVersionValue");
        Mod instance = new Mod("file", "Mod");
        instance.addVersionValue("3.0.0");
        String expResult = "name = \"Mod\"\n" +
"path = \""+DirectoryLookup.getModDir() + "/file.zip\"\n" +
"dependencies = {\n" +
"}\n" +
"supported_version = \"3.0.0\"\n" +
"tags = {\n" +
"\t\"Merge\"\n" +
"}\n";
        assertEquals("Mod differs from expected content",expResult, instance.toString());
    }

    /**
     * Test of getPathValue method, of class Mod.
     * @throws java.io.IOException
     */
    @Test
    public void testGetPathValue() throws IOException {
        System.out.println("getPathValue");
        assertEquals(DirectoryLookup.getModDir() +"/file", new Mod("file", "Mod").getPathValue());
    }

    /**
     * Test of addDepedencyValue method, of class Mod.
     * @throws java.io.IOException
     */
    @Test
    public void testAddDepedencyValue() throws IOException {
        System.out.println("addDepedencyValue");
        Mod instance = new Mod("file", "Mod");
        instance.addDepedencyValue("MyOtherMod");
        String expResult = "name = \"Mod\"\n" +
"path = \""+DirectoryLookup.getModDir() + "/file.zip\"\n" +
"dependencies = {\n" +
"\t\"MyOtherMod\"\n" +
"}\n" +
"supported_version = \"1.0.*\"\n" +
"tags = {\n" +
"\t\"Merge\"\n" +
"}\n";
        assertEquals("Mod differs from expected content",expResult, instance.toString());
    }

    /**
     * Test of addTagValue method, of class Mod.
     * @throws java.io.IOException
     */
    @Test
    public void testAddTagValue() throws IOException {
        System.out.println("addTagValue");
        Mod instance = new Mod("file", "Mod");
        instance.addTagValue("MyOtherMod");
        String expResult = "name = \"Mod\"\n" +
"path = \""+DirectoryLookup.getModDir() + "/file.zip\"\n" +
"dependencies = {\n" +
"}\n" +
"supported_version = \"1.0.*\"\n" +
"tags = {\n" +
"\t\"Merge\"\n" +
"\t\"MyOtherMod\"\n" +
"}\n";
        assertEquals("Mod differs from expected content",expResult, instance.toString());
    }
    
}
