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

import de.idrinth.stellaris.modtools.access.DirectoryLookup;
import de.idrinth.stellaris.modtools.entity.PatchedFile;
import de.idrinth.stellaris.modtools.step.abstracts.TaskList;
import de.idrinth.stellaris.modtools.ziphelpers.Mod;
import de.idrinth.stellaris.modtools.ziphelpers.StreamFromText;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;

public class CreateMod extends TaskList{
    private final String prefix;
    private final Mod mod = new Mod();
    ParallelScatterZipCreator scatterZipCreator = new ParallelScatterZipCreator();
    public CreateMod() {
        super(null);
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EYYYYMMddHHmmss"));
        prefix = "!!!_idrinths-auto-patch_"+time;
        mod.addValueTo("name", "!!!Automatic Patch "+LocalDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME));
        mod.addValueTo("path", "mod/"+prefix+".zip");
    }

    @Override
    protected void fill() throws IOException {
        getEntityManager()
                .createNamedQuery("patched.able", PatchedFile.class)
                .getResultList().stream().map((file) -> {
                    scatterZipCreator.addArchiveEntry(
                            new ZipArchiveEntry(file.getOriginal().getRelativePath()),
                            new StreamFromText(file.getContent().toString())
                    );
            return file;
        }).forEachOrdered((file) -> {
            file.getModifications().forEach((m) -> {
                mod.addValueTo("dependencies", m.getName());
            });
        });
        scatterZipCreator.addArchiveEntry(
            new ZipArchiveEntry("descriptor.mod"),
            new StreamFromText(mod.toString())
        );
        String target = DirectoryLookup.getModDir()+prefix;
        try {
            scatterZipCreator.writeTo(new ZipArchiveOutputStream(new File(target+".zip")));
            FileUtils.write(
                    new File(target+".mod"),
                    mod.toString(),
                    Charset.forName("utf-8")
            );
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(CreateMod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    protected String getIdentifier() {
        return "";
    }
}
