package de.idrinth.stellaris.modtools.model;

import de.idrinth.stellaris.modtools.exception.FailedPatch;
import java.util.HashMap;
import java.util.LinkedList;
import name.fraser.neil.plaintext.diff_match_patch.Patch;
import name.fraser.neil.plaintext.diff_match_patch;

public class Collision {
    protected String original;
    protected String file;
    protected HashMap<String,LinkedList<Patch>> diffs = new HashMap<>();
    protected ModCollection collection;
    protected PatchedFile patch;
    protected diff_match_patch patcher;
    Collision(String original, ModCollection collection, String file, diff_match_patch patcher) {
        this.original = original;
        this.file = file;
        this.patcher = patcher;
        this.collection = collection;
    }
    public void add(String modded,String key) {
        diffs.put(key, patcher.patch_make(original, modded));
        patch = null;
    }
    public PatchedFile get() throws FailedPatch {
        if(patch==null) {
            String result = original;
            HashMap<String,Mod> mods = new HashMap<>();
            for(String key:diffs.keySet()) {
                if(notOverwritten(key)) {
                    mods.put(key,collection.getMods().get(key));
                    Object[] resultList = patcher.patch_apply(diffs.get(key), result);
                    result = (String) resultList[0];
                    boolean[] pL = (boolean[]) resultList[1];
                    for(int c=1;c<pL.length;c++) {
                        if(!pL[c]) {
                            throw new FailedPatch("Patching failed");
                        }
                    }
                }
            }
            patch = new PatchedFile(result,mods,file);
        }
        return patch;
    }
    protected boolean notOverwritten(String key) {
        return diffs.keySet().stream().noneMatch((modKey) -> (collection.getMods().get(modKey).getOverwrites().contains(key)));
    }
    @Override
    public String toString() {
        if(!get().isPatched()) {
            return "X "+file;
        }
        return "O "+file;
    }
}
