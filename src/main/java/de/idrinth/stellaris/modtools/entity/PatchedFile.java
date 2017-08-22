package de.idrinth.stellaris.modtools.entity;

public class PatchedFile {
    protected String content;
    protected int mods;
    protected String file;

    public PatchedFile(String content, int mods, String file) {
        this.file = file;
        this.content = content;
        this.mods = mods;
    }

    public String getContent() {
        return content;
    }

    public String getFile() {
        return file;
    }

    public boolean isPatched() {
        return mods>1;
    }

}
