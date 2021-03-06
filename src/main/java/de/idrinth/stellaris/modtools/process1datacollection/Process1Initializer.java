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

import de.idrinth.stellaris.modtools.filesystem.DirectoryNotFoundException;
import de.idrinth.stellaris.modtools.process.AbstractQueueInitializer;
import de.idrinth.stellaris.modtools.filesystem.ModLocation;
import de.idrinth.stellaris.modtools.process.DataInitializer;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Process1Initializer extends AbstractQueueInitializer implements DataInitializer {
    @Override
    protected void init() {
        try {
            ModLocation mloc = new ModLocation();
            for (File mod : mloc.get().listFiles(new FileExtFilter("mod"))) {
                tasks.add(new ConfigParser(mod, mloc));
            }
        } catch (DirectoryNotFoundException ex) {
            Logger.getLogger(Process1Initializer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
