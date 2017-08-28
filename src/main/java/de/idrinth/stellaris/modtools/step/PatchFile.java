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

import com.sksamuel.diffpatch.DiffMatchPatch;
import de.idrinth.stellaris.modtools.entity.Original;
import de.idrinth.stellaris.modtools.entity.Patch;
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
    protected PatchedFile applyPatches(Original original, ArrayList<Long> ignores, String data) {
        PatchedFile pf = new PatchedFile();
        pf.setRelativePath(file);
        pf.setImportance(file.endsWith(".txt")?2:file.endsWith(".yml")?1:0);
        boolean patchable = file.endsWith(".txt") || file.endsWith(".yml");
        DiffMatchPatch patcher = new DiffMatchPatch();
        for (Patch patch:original.getPatches()) {
            if(!ignores.contains(patch.getMod().getAid())) {
                pf.getModifications().add(patch.getMod());
                if(patchable) {
                    Object[] dat = patcher.patch_apply(patcher.patch_make(original.getContent(), patch.getDiff()), data);
                    for(boolean s:(boolean[]) dat[1]) {
                        patchable = patchable && s;
                    }
                    data = patchable?(String) dat[0]:"Failed patching automatically";
                }
            }
        }
        if(pf.getModifications().size()<2) {
            data = "No patching needed";
        }
        pf.setContent(data);
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
        boolean patchable = file.endsWith(".txt") || file.endsWith(".yml");
        String data = patchable?original.getContent():"file type can't be patched, but shouldn't break anything.";
        ArrayList<Long> ignores = getOverwrittenMods(original);
        manager.persist(applyPatches(original,ignores,data));
        manager.getTransaction().commit();
    }
}
