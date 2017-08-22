package de.idrinth.stellaris.modtools.entity;

import de.idrinth.stellaris.modtools.access.StellarisFile;
import java.util.HashMap;
import name.fraser.neil.plaintext.diff_match_patch;

public class ModCollection {
    protected HashMap<String,Collision> files = new HashMap<>();
    protected HashMap<String,Mod> mods = new HashMap<>();
    protected diff_match_patch patcher = new diff_match_patch();

    public void add(Mod mod) {
        String key = mod.getId() > 0?String.valueOf(mod.getId()):mod.getName();
        if(mods.containsKey(key) && mods.get(key).broken) {
            return;
        }
        mods.put(key, mod);
        mods.get(key).getFiles().stream().map((file) -> {
            if(!files.containsKey(file)) {
                files.put(file, new Collision(StellarisFile.get(file),this, file, patcher));
            }
            return file;
        }).forEachOrdered((file) -> {
            files.get(file).add(mod.getFileContent(file), key);
        });
    }

    public HashMap<String, Collision> getFiles() {
        return files;
    }

    public HashMap<String, Mod> getMods() {
        return mods;
    }
}
