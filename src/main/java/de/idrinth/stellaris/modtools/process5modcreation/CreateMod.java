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

import de.idrinth.stellaris.modtools.entity.PatchedFile;
import de.idrinth.stellaris.modtools.process.ProcessTask;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;

class CreateMod implements ProcessTask {

    private final Mod mod;
    private final String id;

    public CreateMod() {
        id = convertBase10To62(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMddHHmmss")));
        mod = new Mod("idrinths-auto-patch_" + id, "!!!Automatic Patch " + id);
    }

    private String convertBase10To62(String input) {
        Long b10 = Long.parseLong(input, 10);
        StringBuilder ret = new StringBuilder();
        String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        while (b10 > 0) {
            ret.append(characters.charAt((int) (b10 % 62)));
            b10 /= 62;
        }
        return ret.reverse().toString();
    }

    private ZipArchiveEntry makeEntry(String lpath) {
        ZipArchiveEntry entry = new ZipArchiveEntry(lpath);
        entry.setMethod(ZipArchiveEntry.STORED);
        return entry;
    }

    protected void fill(EntityManager manager) throws IOException {
        ParallelScatterZipCreator scatterZipCreator = new ParallelScatterZipCreator();
        if (!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        List<PatchedFile> patches = manager
                .createNamedQuery("patched.able", PatchedFile.class)
                .getResultList();
        if (patches.isEmpty()) {
            return;
        }
        Version version = new Version();
        patches.stream().map((file) -> {
            scatterZipCreator.addArchiveEntry(
                    makeEntry(file.getOriginal().getRelativePath()),
                    new StreamFromText(file.getContent())
            );
            return file;
        }).forEachOrdered((file) -> {
            file.getModifications().forEach((m) -> {
                mod.addDepedencyValue(m.getName());
                version.addIfBigger(m.getVersion());
            });
        });
        try {
            mod.addVersionValue(version.toString());
            scatterZipCreator.addArchiveEntry(
                    makeEntry("descriptor.mod"),
                    new StreamFromText(mod.toString())
            );
            try (ZipArchiveOutputStream out = new ZipArchiveOutputStream(new File(mod.getPathValue() + ".zip"))) {
                scatterZipCreator.writeTo(out);
            }
            FileUtils.write(
                    new File(mod.getPathValue() + ".mod"),
                    mod.toString(),
                    Charset.forName("utf-8")
            );
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(CreateMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        manager.getTransaction().commit();
    }

    @Override
    public String getIdentifier() {
        return String.valueOf(id);
    }

    @Override
    public List<ProcessTask> handle(EntityManager manager) {
        try {
            fill(manager);
        } catch (IOException ex) {
            Logger.getLogger(CreateMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
}
