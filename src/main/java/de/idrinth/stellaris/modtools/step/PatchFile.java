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

import de.idrinth.stellaris.modtools.entity.LazyText;
import de.idrinth.stellaris.modtools.service.MultiDiffPatch;
import de.idrinth.stellaris.modtools.entity.Original;
import de.idrinth.stellaris.modtools.entity.PatchedFile;
import de.idrinth.stellaris.modtools.step.abstracts.TaskList;
import javax.persistence.EntityManager;

public class PatchFile extends TaskList {
    private final String file;
    public PatchFile(String file) {
        super(null);
        this.file = file;
    }
    protected PatchedFile applyPatches(Original original) {
        System.out.println("patching "+file);
        PatchedFile pf = new PatchedFile();
        pf.setOriginal(original);
        pf.setImportance(file.endsWith(".txt")?2:file.endsWith(".yml")?1:0);
        MultiDiffPatch mdp = new MultiDiffPatch(file.endsWith(".txt") || file.endsWith(".yml"),original.getContent().toString());
        original.getPatches().stream().map((patch) -> {
            pf.getModifications().add(patch.getMod());
            System.out.println(patch.getId()+" will be patched in "+file);
            return patch;
        }).forEachOrdered((patch) -> {
            mdp.addText(patch.getDiff().toString());
        });
        if(null == pf.getContent()) {
            pf.setContent(new LazyText());
            getEntityManager().persist(pf.getContent());
        }
        pf.setContent(mdp.getResult());
        return pf;
    }
    @Override
    protected void fill() {
        EntityManager manager = getEntityManager();
        if(!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        Original original = (Original) manager.find(Original.class, file);
        if(original.getPatches().size()<2) {
            System.out.println(file+" is without conflicts");
            manager.getTransaction().commit();
            return;//nothing relevant, no patching required
        }
        manager.persist(applyPatches(original));
        manager.getTransaction().commit();
    }

    @Override
    protected String getIdentifier() {
        return file;
    }
}
