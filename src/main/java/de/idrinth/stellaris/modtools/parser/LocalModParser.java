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
package de.idrinth.stellaris.modtools.parser;

import com.github.sarxos.winreg.RegistryException;
import de.idrinth.stellaris.modtools.access.DatabaseRetriever;
import de.idrinth.stellaris.modtools.access.DirectoryLookup;
import de.idrinth.stellaris.modtools.access.StellarisFileContent;
import de.idrinth.stellaris.modtools.entity.ModFile;
import de.idrinth.stellaris.modtools.entity.ModKey;
import de.idrinth.stellaris.modtools.entity.Modification;
import de.idrinth.stellaris.modtools.entity.StellarisFile;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.sksamuel.diffpatch.DiffMatchPatch;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class LocalModParser extends RemoteModParser {

    private final DiffMatchPatch patcher = new DiffMatchPatch();

    public LocalModParser(String id) {
        super(id);
    }

    private File getWithPrefix(String path, String prefix) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            return file;
        }
        file = new File(prefix + "\\" + path);
        if (file.exists()) {
            return file;
        }
        throw new IOException("No valid path to mod found: " + file.getAbsolutePath());
    }

    protected void getConfig(File file, Modification mod) throws IOException, RegistryException {
        for (String line : FileUtils.readFileToString(file, "utf-8").split("\\r?\\n\\r?")) {
            if (null != line && line.matches("\\s*[a-z_]+\\s*=\\s*\".*?\"")) {
                String[] parts = line.trim().split("=", 2);
                String value = parts[1].trim().replaceAll("^\"|\"$", "");
                switch (parts[0].trim()) {
                    case "archive"://downloaded mod
                        handlePath(getWithPrefix(value, DirectoryLookup.getSteamDir().getAbsolutePath()).getAbsolutePath(), mod);
                        break;
                    case "remote_file_id"://downloaded mod - ignore self-collisions
                        ModKey key = (ModKey) DatabaseRetriever.get(ModKey.class, "ugc_" + value);
                        key.setModification(mod);
                        break;
                    case "name":
                        mod.setName(value);
                        break;
                    case "path"://local mod
                        handlePath(getWithPrefix(value, DirectoryLookup.getModDir().getParent()).getAbsolutePath(), mod);
                        break;
                    case "supported_version":
                        mod.setVersion(value);
                        break;
                    case "picture":
                        break;
                    default:
                    //TODO: tags, dependencies, image?
                }
            }
        }
    }

    protected void fillFilesFromZip(String path, String[] exts, Modification mod) throws IOException {
        try (ZipFile zip = new ZipFile(path)) {
            Enumeration entries = zip.getEntries();
            while (entries.hasMoreElements()) {
                handleSingleZipEntry(zip, exts, (ZipArchiveEntry) entries.nextElement(), mod);
            }
        }
    }

    protected void handleSingleZipEntry(ZipFile zip, String exts[], ZipArchiveEntry entry, Modification mod) throws IOException {
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

    protected void fillFilesFromFolder(String path, String[] exts, Modification mod) throws IOException {
        FileUtils.listFiles(new File(path), exts, true).forEach((file) -> {
            try {
                addToFiles(file.getAbsolutePath().substring(path.length()), mod, FileUtils.readFileToString(file, "utf-8"));//relative path
            } catch (IOException ex) {
                Logger.getLogger(LocalModParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    protected void addToFiles(String fPath, Modification mod, String content) {
        if (fPath.contains("/")) {
            StellarisFile file = (StellarisFile) DatabaseRetriever.get(StellarisFile.class, fPath);
            ModFile mf = new ModFile(mod, file);
            if (null == file.getMods()) {
                file.setMods(new HashSet<>());
                file.setPatchable(fPath.endsWith(".txt") || fPath.endsWith(".yml"));
                file.setContent(StellarisFileContent.get(fPath));
            }
            file.getMods().add(mf);
            mf.setDiff(file.isPatchable() ? patcher.patch_toText(patcher.patch_make(mf.getFile().getContent(), content)) : content);
            if (null == mod.getFiles()) {
                mod.setFiles(new HashSet<>());
            }
            mod.getFiles().add(mf);
        }
    }

    private void handlePath(String path, Modification mod) throws IOException {
        String[] ext = ".txt,.yml,.dds,.gfx,.gui".split(",");
        if (path.endsWith(".zip")) {
            fillFilesFromZip(path, ext, mod);
        } else {
            fillFilesFromFolder(path, ext, mod);
        }
    }

    @Override
    protected void fill(Modification mod) {
        if (id.startsWith("ugc_")) {
            super.fill(mod);
        }
        try {
            File config = new File(DirectoryLookup.getModDir().toURI() + id);
            if (config.exists()) {
                getConfig(config, mod);
            }
        } catch (Exception ex) {

        }
    }
}
