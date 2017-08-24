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
package de.idrinth.stellaris.modtools.model;

import de.idrinth.stellaris.modtools.access.SteamDescription;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.compress.archivers.zip.ZipFile;

public class Mod{
    protected ModCollection list;
    //basics
    protected String name;
    protected String version;
    protected String path;
    //Remote only
    protected int id=0;
    protected String description;
    //generated
    protected ArrayList<String> files = new ArrayList<>();
    protected ArrayList<String> overwrites = new ArrayList<>();
    public boolean broken = false;

    public Mod(ModCollection list) {
        this.list = list;
    }
    public void lock() {
        this.list.add(this);
    }

    public ModCollection getList() {
        return list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        new Thread(new SteamDescription(this)).start();
    }
    protected void fillFilesFromZip(String[] exts) throws IOException {
        try (ZipFile zip = new ZipFile(path)) {
            Enumeration entries = zip.getEntries();
            while(entries.hasMoreElements()) {
                ZipArchiveEntry entry = (ZipArchiveEntry) entries.nextElement();
                for(String ext:exts) {
                    if(entry.getName().endsWith(ext)) {
                        addToFiles(entry.getName());
                    }
                }
            }
        }
    }
    protected void fillFilesFromFolder(String[] exts) throws IOException {
        FileUtils.listFiles(new File(path), exts, true).forEach((file) -> {
            addToFiles(file.getAbsolutePath().substring(path.length()));//relative path
        });
    }
    protected void addToFiles(String fPath) {
        if(fPath.contains("/")) {
            files.add(fPath);
        }
    }
    public void setPath(String path) {
        this.path = path;
        String[] ext = ".txt,.yml,.dds,.gfx,.gui".split(",");
        files.clear();
        try {
            if(path.endsWith(".zip")) {
                fillFilesFromZip(ext);
            } else {
                fillFilesFromFolder(ext);
            }
        } catch (IOException exception) {
            broken = true;
            description = exception.getLocalizedMessage();
        }
    }
    
    protected ArrayList<String> getFiles() {
        return files;
    }

    public String getDescription() {
        return "<h1>"+name+"</h1>"+description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getOverwrites() {
        return overwrites;
    }

    public String getFileContent(String relativePath) {
        return "";
    }

    @Override
    public int hashCode() {
        return 11*(77 + Objects.hashCode(this.name)) + this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Mod other = (Mod) obj;
        return id == other.getId() && name.equals(other.getName());
    }
    @Override
    public String toString() {
        return (broken?"BROKEN: ":"")+(name == null?path:name)+(id>0?" ("+String.valueOf(id)+")":"");
    }
}
