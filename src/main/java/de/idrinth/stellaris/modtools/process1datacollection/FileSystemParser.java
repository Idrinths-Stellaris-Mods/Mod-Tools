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

import de.idrinth.stellaris.modtools.process.ProcessTask;
import de.idrinth.stellaris.modtools.filesystem.FileExtensions;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.apache.commons.io.FileUtils;

class FileSystemParser extends Files {

    private final File folder;

    public FileSystemParser(String modConfigName, File folder) {
        super(modConfigName);
        this.folder = folder;
    }

    @Override
    protected void addToFiles(String fPath, String content, EntityManager manager) {
        if (!(fPath.contains("/") || fPath.contains("\\"))) {
            return;
        }
        super.addToFiles(fPath, content, manager);
    }

    @Override
    public List<ProcessTask> handle(EntityManager manager) {
        if (!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        int pathLength = folder.getAbsolutePath().length();
        FileUtils.listFiles(folder, FileExtensions.getPatchable(), true).forEach((file) -> {
            try {
                addToFiles(file.getAbsolutePath().substring(pathLength), FileUtils.readFileToString(file, "utf-8"), manager);//relative relativePath
            } catch (IOException ex) {
                Logger.getLogger(FileSystemParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        FileUtils.listFiles(folder, FileExtensions.getReplaceable(), true).forEach((file) -> {
            addToFiles(file.getAbsolutePath().substring(pathLength), "", manager);//relative relativePath
        });
        return todo;
    }
}
