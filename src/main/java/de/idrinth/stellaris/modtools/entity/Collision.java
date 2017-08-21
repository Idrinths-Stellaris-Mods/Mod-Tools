package de.idrinth.stellaris.modtools.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class Collision {
    protected String file;
    protected ArrayList<Mod> inMods = new ArrayList<>();
    protected HashSet<Mod> resolved = new HashSet<>();
    protected boolean solved=true;

    public Collision(String file,Mod mod) {
        this.file = file;
        addMod(mod);
    }
    public final void addMod(Mod mod) {
        inMods.add(mod);
        resolved.addAll(mod.getOverwrites());
        solved = resolved.size() == inMods.size()-1;
    }
    public ArrayList<String> getUnresolved() {
        ArrayList<String> unresolved = new ArrayList<>();
        if(solved) {
            return unresolved;
        }
        inMods.stream().filter((mod) -> (!resolved.contains(mod))).forEachOrdered((mod) -> {
            unresolved.add(mod.name);
        });
        return unresolved;
    }

    public String getFile() {
        return file;
    }

    @Override
    public int hashCode() {
        return 19 * 7 + Objects.hashCode(this.file);
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
        final Collision other = (Collision) obj;
        return file.equals(other.getFile());
    }
}
