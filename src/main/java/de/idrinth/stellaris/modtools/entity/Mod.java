package de.idrinth.stellaris.modtools.entity;

import de.idrinth.stellaris.modtools.access.SteamDescription;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.compress.archivers.zip.ZipFile;

public class Mod {
    protected ModList list;
    protected boolean locked=false;
    //basics
    protected String name;
    protected String[] tags;
    protected String version;
    //Remote only
    protected int id=0;
    protected String description;
    //generated
    protected ArrayList<String> files = new ArrayList<>();
    protected ArrayList<Mod> overwrites = new ArrayList<>();
    public boolean broken = false;

    public Mod(ModList list) {
        this.list = list;
    }
    public void lock() {
        locked=true;
        this.list.add(this);
    }

    public ModList getList() {
        return list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(locked) {
            throw new UnsupportedOperationException("The class is locked.");
        }
        this.name = name;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setArchive(String archive) throws IOException {
        files.clear();
        Enumeration entries = new ZipFile(archive).getEntries();
        String[] exts = ".txt,.yml,.dds,.gfx,.gui".split("/,/");
        while(entries.hasMoreElements()) {
            ZipArchiveEntry entry = (ZipArchiveEntry) entries.nextElement();
            for(String ext:exts) {
                if(entry.getName().endsWith(ext)) {
                    files.add(entry.getName());
                }
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if(locked) {
            throw new UnsupportedOperationException("The class is locked.");
        }
        this.id = id;
        new Thread(new SteamDescription(this)).start();
    }

    public void setPath(String path) {
        String[] ext = "txt,yml,dds,gfx,gui".split("/,/");
        files.clear();
        FileUtils.listFiles(new File(path), ext, true).forEach((file) -> {
            files.add(file.getAbsolutePath().substring(path.length()));//relative path
        });
    }
    
    protected ArrayList<String> getFiles() {
        return files;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Mod> getOverwrites() {
        return overwrites;
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
}
