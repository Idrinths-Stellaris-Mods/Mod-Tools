package de.idrinth.stellaris.modtools.filter;

import java.io.File;

public class FileExt implements java.io.FilenameFilter {

    protected String extension;
    public FileExt(String extension) {
        this.extension = "."+extension;
    }
    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(extension);
    }
}
