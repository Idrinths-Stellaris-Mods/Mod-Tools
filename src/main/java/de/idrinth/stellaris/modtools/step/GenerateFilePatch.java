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
import de.idrinth.stellaris.modtools.entity.Patch;
import de.idrinth.stellaris.modtools.step.abstracts.TaskList;
import javax.persistence.EntityManager;

public class GenerateFilePatch extends TaskList {
    private final long id;
    public GenerateFilePatch(long id) {
        super(null);
        this.id = id;
    }
    @Override
    protected void fill() {
        EntityManager manager = getEntityManager();
        if(!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        Patch patch = (Patch) manager.find(Patch.class, id);
        if(patch.getFile().getRelativePath().endsWith(".txt") || patch.getFile().getRelativePath().endsWith(".yml")) {
            DiffMatchPatch dmp = new DiffMatchPatch();
            String result = patch.getDiff().toString();
            String original = patch.getFile().getContent().toString();
            patch.getDiff().setText(dmp.patch_toText(dmp.patch_make(original, result)));
        }
        manager.getTransaction().commit();
    }

    @Override
    protected String getIdentifier() {
        return String.valueOf(id);
    }
}
