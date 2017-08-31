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
package de.idrinth.stellaris.modtools.step;

import de.idrinth.stellaris.modtools.access.Queue;
import de.idrinth.stellaris.modtools.service.FileExtensions;
import de.idrinth.stellaris.modtools.step.abstracts.Files;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.apache.commons.io.FileUtils;

public class FileSystemParser extends Files implements Runnable {
    private final File folder;

    public FileSystemParser(String modConfigName, File folder, Queue queue) {
        super(queue, modConfigName);
        this.folder = folder;
    }

    @Override
    protected void fill() throws IOException {
        EntityManager manager = getEntityManager();
        if(!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        int pathLength = folder.getAbsolutePath().length();
        FileUtils.listFiles(folder, FileExtensions.getPatchable(), true).forEach((file) -> {
            try {
                addToFiles(file.getAbsolutePath().substring(pathLength), FileUtils.readFileToString(file, "utf-8"));//relative relativePath
            } catch (IOException ex) {
                Logger.getLogger(FileSystemParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        FileUtils.listFiles(folder, FileExtensions.getReplaceable(), true).forEach((file) -> {
            addToFiles(file.getAbsolutePath().substring(pathLength), "");//relative relativePath
        });
    }
    @Override
    protected void addToFiles(String fPath, String content) {        
        if (!(fPath.contains("/")||fPath.contains("\\"))) {
            return;
        }
        super.addToFiles(fPath, content);
    }
}
