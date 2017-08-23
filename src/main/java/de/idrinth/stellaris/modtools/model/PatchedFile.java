package de.idrinth.stellaris.modtools.model;

import java.util.HashMap;

public class PatchedFile {
    protected String content;
    protected HashMap<String,Mod> mods;
    protected String file;

    public PatchedFile(String content, HashMap<String,Mod> mods, String file) {
        this.file = file;
        this.content = content;
        this.mods = mods;
    }

    public String getContent() {
        return "<h1>"+file+"</h1>"+getLinks()+"<pre>"+content+"</pre>";
    }

    private String getLinks() {
        String modstring = "<ul>";
        for(Mod mod:mods.values()) {
            modstring = modstring+"<li><a href=\"http://steamcommunity.com/sharedfiles/filedetails/?id="+mod.getId()+"\">"+mod.getName()+"</a></li>";
        }
        return modstring+"</ul>";
    }

    public String getFile() {
        return file;
    }

    public boolean isPatched() {
        return mods.size()>1;
    }

    public String getMods() {
        String modstring = "";
        for(String key:mods.keySet()) {
            modstring = modstring+"\n"+key;
        }
        return modstring.substring(1);
    }

}
