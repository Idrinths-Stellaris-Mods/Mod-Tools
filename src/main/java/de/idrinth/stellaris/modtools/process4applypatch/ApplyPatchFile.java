/*
 * Copyright (C) 2017 Björn Büttner
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
package de.idrinth.stellaris.modtools.process4applypatch;

import com.sksamuel.diffpatch.DiffMatchPatch;
import de.idrinth.stellaris.modtools.entity.Patch;
import de.idrinth.stellaris.modtools.entity.PatchedFile;
import de.idrinth.stellaris.modtools.process.ProcessTask;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;

class ApplyPatchFile implements ProcessTask {

    private final long patchId;
    private final long target;
    private final LinkedList<Long> next;

    public ApplyPatchFile(LinkedList<Long> patches, long target) {
        if(patches.isEmpty()) {
            throw new IllegalArgumentException("patch list must not be empty");
        }
        this.patchId = patches.poll();
        this.target = target;
        this.next = patches;
    }

    protected void patch(PatchedFile pf, Patch patch) {
        if (!pf.isPatchable()) {
            return;
        }
        DiffMatchPatch patcher = new DiffMatchPatch();
        Object[] dat = patcher.patch_apply((LinkedList) patcher.patch_fromText(patch.getDiff()), pf.getContent());
        for (boolean s : (boolean[]) dat[1]) {
            pf.setPatchable(pf.isPatchable() && s);
        }
        pf.setContent(pf.isPatchable() ? (String) dat[0] : "Failed patching automatically");
    }

    @Override
    public String getIdentifier() {
        return String.valueOf(patchId) + "->" + String.valueOf(target);
    }

    @Override
    public List<ProcessTask> handle(EntityManager manager) {
        ArrayList<ProcessTask> tasks = new ArrayList<>();
        if (!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        Patch patch = (Patch) manager.find(Patch.class, patchId);
        PatchedFile pf = (PatchedFile) manager.find(PatchedFile.class, target);

        patch(pf, patch);

        pf.getModifications().add(patch.getMod());

        manager.getTransaction().commit();
        if (!next.isEmpty()) {
            tasks.add(new ApplyPatchFile(next, target));
        }
        return tasks;
    }
}
