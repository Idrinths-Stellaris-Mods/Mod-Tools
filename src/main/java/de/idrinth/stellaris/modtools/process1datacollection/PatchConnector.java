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

import de.idrinth.stellaris.modtools.process.AbstractQueue;
import de.idrinth.stellaris.modtools.entity.Modification;
import de.idrinth.stellaris.modtools.entity.Original;
import de.idrinth.stellaris.modtools.entity.Patch;
import de.idrinth.stellaris.modtools.process.Task;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

class PatchConnector extends Task {

    private final long patchId;
    private final long modId;
    private final String path;

    public PatchConnector(long patchId, long modId, String path, AbstractQueue queue) {
        super(queue);
        this.patchId = patchId;
        this.modId = modId;
        this.path = path;
    }

    @Override
    protected void fill() throws IOException {
        EntityManager manager = getEntityManager();
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
            tasks.add(new OriginalFileFiller(path));
            manager.persist(file);
        }
        Patch patch = (Patch) manager.find(Patch.class, patchId);
        patch.setMod(mod);
        patch.setFile(file);
        mod.getPatches().add(patch);
        file.getPatches().add(patch);
        manager.getTransaction().commit();
    }

    @Override
    protected String getIdentifier() {
        return modId + "|" + patchId + "|" + path;
    }

}
