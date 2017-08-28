/*
 * Copyright (C) 2017 Idrinth
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
import de.idrinth.stellaris.modtools.entity.Modification;
import de.idrinth.stellaris.modtools.step.abstracts.Files;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import javax.persistence.EntityManager;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Björn
 */
public class ZipContentParser extends Files implements Runnable {
    private final File file;

    public ZipContentParser(String modConfigName, File file, Queue queue) {
        super(queue, modConfigName);
        this.file = file;
    }

    @Override
    protected void fill() throws IOException {
        EntityManager manager = getEntityManager();
        if(!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        Modification mod = (Modification) manager.createNamedQuery("modifications.config", Modification.class).setParameter("configPath", modConfigName).getSingleResult();

        try (ZipFile zip = new ZipFile(file)) {
            Enumeration entries = zip.getEntries();
            while (entries.hasMoreElements()) {
                handleSingleZipEntry(zip, (ZipArchiveEntry) entries.nextElement(), mod);
            }
        }
        manager.persist(mod);
        manager.getTransaction().commit();
    }

    protected void handleSingleZipEntry(ZipFile zip, ZipArchiveEntry entry, Modification mod) throws IOException {
        if (entry.isDirectory()) {
            return;
        }
        for (String ext : exts) {
            if (entry.getName().endsWith(ext)) {
                addToFiles(entry.getName(), mod, IOUtils.toString(zip.getInputStream(entry), "utf-8"));
                return;
            }
        }
    }
    
}
