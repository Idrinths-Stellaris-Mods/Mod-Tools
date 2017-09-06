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

import de.idrinth.stellaris.modtools.abstract_cases.TestATask;
import de.idrinth.stellaris.modtools.filesystem.FileSystemLocation;
import de.idrinth.stellaris.modtools.persistence.PersistenceProvider;
import de.idrinth.stellaris.modtools.persistence.entity.Modification;
import de.idrinth.stellaris.modtools.process.ProcessTask;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

public class ConfigParserTest extends TestATask {

    @Override
    protected ProcessTask get() {
        return get(new File("./ConfigParserTest/cf0.mod"));
    }

    protected ProcessTask get(File file) {
        return new ConfigParser(file, new ModDirFake());
    }

    private void checkHandlePart(File file, String content, int results, String name, int id, String version) throws IOException {
            EntityManager manager = new PersistenceProvider().get();
            file.getParentFile().mkdirs();
                FileUtils.writeStringToFile(file, content, "utf-8");
            List<ProcessTask> result = get(file).handle(manager);
            Assert.assertTrue(
                file.getName() + ": Wrong type returned",
                result instanceof List<?>
            );
            Assert.assertEquals(
                file.getName() + ": Wrong number of tasks returned",
                results,
                result.size()
            );
            Modification mod = manager.createNamedQuery("modifications.config", Modification.class)
                    .setParameter("configPath", file.getName())
                    .getSingleResult();
            Assert.assertEquals(
                file.getName() + ": Mod has wrong id",
                id,
                mod.getId()
            );
            Assert.assertEquals(
                file.getName() + ": Mod has wrong name",
                name,
                mod.getName()
            );
            Assert.assertEquals(
                file.getName() + ": Mod has wrong version",
                version,
                mod.getVersion()
            );
            manager.remove(mod);
    }
    /**
     * Test of handle method, of class ConfigParser.
     */
    @Test
    public void testHandle() {
        System.out.println("handle");
        try {
            // local folder
            new File("./ConfigParserTest/local").mkdirs();
            checkHandlePart(
                new File("./ConfigParserTest/cf.mod"),
                    "name = \"Mod\"\npath = \"ConfigParserTest/local\"\ndependencies = {\n}\nsupported_version = \"1.0.*\"\ntags = {\n\t\"Merge\"\n}\n",
                    1,
                    "Mod",
                    0,
                    "1.0.*"
            );
            // remote zip
            new File("./ConfigParserTest/remote.zip").createNewFile();
            checkHandlePart(
                new File("./ConfigParserTest/cf2.mod"),
                    "name = \"Mod2\"\narchive = \"ConfigParserTest/remote.zip\"\nremote_file_id = \"3210\"\ndependencies = {\n}\nsupported_version = \"1.9.*\"\ntags = {\n\t\"Merge\"\n}\n",
                    2,
                    "Mod2",
                    3210,
                    "1.9.*"
            );
        } catch(IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }
    private class ModDirFake implements FileSystemLocation {
        private final File directory;

        public ModDirFake() {
            this(new File("./CreateModTest"));
        }
        public ModDirFake(File directory) {
            this.directory = directory;
            if(!directory.exists()) {
                directory.mkdirs();
            }
        }
        
        @Override
        public File get() {
            return directory;
        }
    }
}
