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

import de.idrinth.stellaris.modtools.process.ProcessHandlingQueue;
import de.idrinth.stellaris.modtools.entity.Original;
import de.idrinth.stellaris.modtools.entity.PatchedFile;
import de.idrinth.stellaris.modtools.service.FileExtensions;
import de.idrinth.stellaris.modtools.process.Task;
import java.util.LinkedList;
import javax.persistence.EntityManager;

class PatchFile extends Task {

    private final long id;

    public PatchFile(long id, ProcessHandlingQueue queue) {
        super(queue);
        this.id = id;
    }

    @Override
    protected void fill() {
        EntityManager manager = getEntityManager();
        if (!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        Original original = (Original) manager.find(Original.class, id);
        if (original.getPatches().size() < 2) {
            System.out.println(id + " is without conflicts");
            manager.getTransaction().commit();
            return;//nothing relevant, no patching required
        }
        PatchedFile pf = new PatchedFile();
        pf.setOriginal(original);
        pf.setPatchable(FileExtensions.isPatchable(original.getRelativePath()));
        pf.setPatchableExt(pf.isPatchable());
        pf.setImportance(original.getRelativePath().endsWith(".txt") ? 2 : original.getRelativePath().endsWith(".yml") ? 1 : 0);
        pf.setContent(original.getContent());
        manager.persist(pf);
        LinkedList<Long> ll = new LinkedList<>();
        original.getPatches().forEach((patch) -> {
            ll.add(patch.getAid());
        });
        tasks.add(new ApplyPatchFile(ll, pf.getAid(), queue));
        manager.getTransaction().commit();
    }

    @Override
    protected String getIdentifier() {
        return String.valueOf(id);
    }
}
