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

import de.idrinth.stellaris.modtools.process.Task;
import de.idrinth.stellaris.modtools.process.AbstractQueue;
import de.idrinth.stellaris.modtools.entity.Modification;
import de.idrinth.stellaris.modtools.entity.Patch;
import javax.persistence.EntityManager;

abstract public class Files extends Task {

    protected final String modConfigName;

    public Files(AbstractQueue queue, String modConfigName) {
        super(queue);
        this.modConfigName = modConfigName;
    }

    @Override
    protected String getIdentifier() {
        return modConfigName;
    }

    protected void addToFiles(String fPath, String content) {
        System.out.println(fPath + " being added");
        EntityManager manager = getEntityManager();
        if (!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        Modification mod = (Modification) manager.createNamedQuery("modifications.config", Modification.class)
                .setParameter("configPath", modConfigName)
                .getSingleResult();
        Patch patch = new Patch();
        patch.setDiff(content);
        manager.persist(patch);
        tasks.add(new PatchConnector(patch.getAid(), mod.getAid(), fPath.replace("\\", "/"), queue));
        manager.getTransaction().commit();
        System.out.println(fPath + " was added");
    }

}
