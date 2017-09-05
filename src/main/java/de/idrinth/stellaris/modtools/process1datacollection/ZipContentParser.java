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
import de.idrinth.stellaris.modtools.service.FileExtensions;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.collections4.iterators.EnumerationIterator;
import org.apache.commons.collections4.iterators.IteratorIterable;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.IOUtils;

class ZipContentParser extends Files {

    private final File file;

    public ZipContentParser(String modConfigName, File file) {
        super(modConfigName);
        this.file = file;
    }

    protected void handleSingleZipEntry(ZipFile zip, ZipArchiveEntry entry, EntityManager manager) throws IOException {
        if (entry.isDirectory() || !(entry.getName().contains("/") || entry.getName().contains("\\"))) {
            return;
        }
        if (FileExtensions.isPatchable(entry.getName())) {
            addToFiles(entry.getName(), IOUtils.toString(zip.getInputStream(entry), "utf-8"), manager);
            return;
        }
        if (FileExtensions.isReplaceable(entry.getName())) {
            addToFiles(entry.getName(), "", manager);
        }
    }

    @Override
    public List<ProcessTask> handle(EntityManager manager) {
        if (!file.exists()) {
            return todo;
        }
        try (ZipFile zip = new ZipFile(file)) {
            for (ZipArchiveEntry entry : new IteratorIterable<>(new EnumerationIterator<>(zip.getEntries()))) {
                try {
                    handleSingleZipEntry(zip, entry, manager);
                } catch (IOException ex) {
                    System.out.println(ex.getCause().getLocalizedMessage());
                }
            }
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
        return todo;
    }
}
