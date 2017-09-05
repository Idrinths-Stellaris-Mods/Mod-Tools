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

import de.idrinth.stellaris.modtools.entity.Original;
import de.idrinth.stellaris.modtools.entity.PatchedFile;
import de.idrinth.stellaris.modtools.process.ProcessTask;
import de.idrinth.stellaris.modtools.service.FileExtensions;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;

class PatchFile implements ProcessTask {

    private final long id;
    private final ArrayList<ProcessTask> todo = new ArrayList();

    public PatchFile(long id) {
        this.id = id;
    }

    @Override
    public String getIdentifier() {
        return String.valueOf(id);
    }

    @Override
    public List<ProcessTask> handle(EntityManager manager) {
        if (!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        Original original = (Original) manager.find(Original.class, id);
        if (original.getPatches().size() < 2) {
            System.out.println(id + " is without conflicts");
            manager.getTransaction().commit();
            return todo;//nothing relevant, no patching required
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
        todo.add(new ApplyPatchFile(ll, pf.getAid()));
        manager.getTransaction().commit();
        return todo;
    }
}
