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
package de.idrinth.stellaris.modtools.step;

import de.idrinth.stellaris.service.MultiDiffPatch;
import de.idrinth.stellaris.modtools.entity.Original;
import de.idrinth.stellaris.modtools.entity.PatchedFile;
import de.idrinth.stellaris.modtools.step.abstracts.TaskList;
import java.util.ArrayList;
import javax.persistence.EntityManager;

public class PatchFile extends TaskList {
    private final String file;
    public PatchFile(String file) {
        super(null);
        this.file = file;
    }
    protected ArrayList<Long> getOverwrittenMods(Original original) {
        ArrayList<Long> ignores = new ArrayList<>();
        original.getPatches().forEach((patch) -> {
            patch.getMod().getOverwrite().forEach((mod) -> {
                ignores.add(mod.getAid());
            });
        });
        return ignores;
    }
    protected PatchedFile applyPatches(Original original, ArrayList<Long> ignores) {
        PatchedFile pf = new PatchedFile();
        pf.setRelativePath(file);
        pf.setImportance(file.endsWith(".txt")?2:file.endsWith(".yml")?1:0);
        MultiDiffPatch mdp = new MultiDiffPatch(file.endsWith(".txt") || file.endsWith(".yml"),original.getContent());
        original.getPatches().stream().filter((patch) -> (!ignores.contains(patch.getMod().getAid()))).map((patch) -> {
            pf.getModifications().add(patch.getMod());
            return patch;
        }).forEachOrdered((patch) -> {
            mdp.addText(patch.getDiff());
        });
        pf.setContent(mdp.getResult());
        addCollisionsToMods(pf);
        return pf;
    }
    protected void addCollisionsToMods(PatchedFile pf) {
        if(pf.getModifications().size()<2) {
            return;
        }
        pf.getModifications().forEach((mod) -> {
            pf.getModifications().stream().filter((m) -> (!mod.equals(m))).forEachOrdered((m) -> {
                mod.getCollides().add(m);
            });
        });
    }
    @Override
    protected void fill() {
        EntityManager manager = getEntityManager();
        Original original = (Original) manager.find(Original.class, file);
        if(original.getPatches().size()<2) {
            return;//nothing relevant, no patching required
        }
        if(!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        manager.persist(applyPatches(original,getOverwrittenMods(original)));
        manager.getTransaction().commit();
    }
}
