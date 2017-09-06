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
package de.idrinth.stellaris.modtools.process1datacollection;

import de.idrinth.stellaris.modtools.persistence.entity.Modification;
import de.idrinth.stellaris.modtools.persistence.entity.Original;
import de.idrinth.stellaris.modtools.persistence.entity.Patch;
import de.idrinth.stellaris.modtools.filesystem.FileSystemLocation;
import de.idrinth.stellaris.modtools.process.ProcessTask;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

class PatchConnector implements ProcessTask {

    private final long patchId;
    private final long modId;
    private final String path;
    private final FileSystemLocation steamDir;
    private final ArrayList<ProcessTask> todo = new ArrayList<>();

    public PatchConnector(long patchId, long modId, String path, FileSystemLocation steamDir) {
        this.patchId = patchId;
        this.modId = modId;
        this.path = path;
        this.steamDir = steamDir;
    }

    @Override
    public List<ProcessTask> handle(EntityManager manager) {
        if (!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        Modification mod = (Modification) manager.find(Modification.class, modId);
        Original file;
        try {
            file = (Original) manager.createNamedQuery("original.path", Original.class)
                    .setParameter("path", path)
                    .getSingleResult();
        } catch (NoResultException ex) {
            file = new Original(path);
            todo.add(new OriginalFileFiller(path, steamDir));
            manager.persist(file);
        }
        Patch patch = (Patch) manager.find(Patch.class, patchId);
        patch.setMod(mod);
        patch.setFile(file);
        mod.getPatches().add(patch);
        file.getPatches().add(patch);
        manager.getTransaction().commit();
        return todo;
    }

    @Override
    public String getIdentifier() {
        return modId + "|" + patchId + "|" + path;
    }

}
