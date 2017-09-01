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
package de.idrinth.stellaris.modtools.step;

import com.sksamuel.diffpatch.DiffMatchPatch;
import de.idrinth.stellaris.modtools.access.Queue;
import de.idrinth.stellaris.modtools.entity.Patch;
import de.idrinth.stellaris.modtools.entity.PatchedFile;
import de.idrinth.stellaris.modtools.step.abstracts.TaskList;
import java.util.LinkedList;
import javax.persistence.EntityManager;

public class ApplyPatchFile extends TaskList {
    private final long patchId;
    private final long target;
    private final LinkedList<Long> next;

    public ApplyPatchFile(LinkedList<Long> patches, long target, Queue queue) {
        super(queue);
        this.patchId = patches.poll();
        this.target = target;
        this.next = patches;
    }

    @Override
    protected void fill() {
        EntityManager manager = getEntityManager();
        if(!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        Patch patch = (Patch) manager.find(Patch.class, patchId);
        PatchedFile pf = (PatchedFile) manager.find(PatchedFile.class, target);
        
        patch(pf, patch);
        
        pf.getModifications().add(patch.getMod());
        
        manager.getTransaction().commit();
        if(!next.isEmpty()) {
            tasks.add(new ApplyPatchFile(next,target, queue));
        }
    }
    protected void patch(PatchedFile pf, Patch patch) {
        if(!pf.isPatchable()) {
            return;
        }
        DiffMatchPatch patcher = new DiffMatchPatch();
        Object[] dat = patcher.patch_apply((LinkedList) patcher.patch_fromText(patch.getDiff().toString()), pf.getContent().getText());
        for(boolean s:(boolean[]) dat[1]) {
            pf.setPatchable(pf.isPatchable() && s);
        }
        pf.setContent(pf.isPatchable()?(String) dat[0]:"Failed patching automatically");
    }

    @Override
    protected String getIdentifier() {
        return String.valueOf(patchId)+"->"+String.valueOf(target);
    }
}
