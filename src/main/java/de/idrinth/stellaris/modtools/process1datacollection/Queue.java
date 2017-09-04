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

import de.idrinth.stellaris.modtools.process.FillerThread;
import de.idrinth.stellaris.modtools.service.DirectoryLookup;
import de.idrinth.stellaris.modtools.gui.Progress;
import de.idrinth.stellaris.modtools.process.AbstractQueue;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Queue extends AbstractQueue {

    public Queue(FillerThread c, Progress progress) {
        super(c, progress, "Collecting data", Executors.newFixedThreadPool(20));
    }

    @Override
    protected void addList() {
        try {
            for (File mod : DirectoryLookup.getModDir().listFiles(new FileExtFilter("mod"))) {
                add(new ConfigParser(mod, this));
            }
        } catch (IOException ex) {
            Logger.getLogger(Queue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
