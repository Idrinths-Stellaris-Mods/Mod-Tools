package de.idrinth.stellaris.modtools.entity;

import java.util.HashSet;

public class ModList extends HashSet<Mod> {
    protected static ModList instance;
    public static ModList getInstance() {
        return instance;
    }
    public boolean hasId(int id) {
        return id !=0 && this.stream().anyMatch((mod) -> (mod.getId() == id));
    }
    public Mod getById(int id) {
        if(id>0) {
            for(Mod mod:this) {
                if(mod.getId() == id) {
                    return mod;
                }
            }
        }
        throw new IllegalArgumentException("supplied Id is unknown");
    }
    @Override
    public boolean add(Mod mod) {
        if(super.add(mod)) {
            if(mod.getId() == 0) {
                return true;
            }
            Mod broken = null;
            int count=0;
            for(Mod lmod:this) {
                if(mod.getId() == lmod.getId()) {
                    count++;
                    broken = lmod;
                }
            }
            if(count<2) {
                return true;
            }
            if(broken != null) {
                return remove(broken);
            }
        }
        return false;
    }
}
