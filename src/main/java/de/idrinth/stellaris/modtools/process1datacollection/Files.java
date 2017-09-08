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
import de.idrinth.stellaris.modtools.process.ProcessTask;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

abstract class Files implements ProcessTask {

    protected final String modConfigName;
    protected final ArrayList<ProcessTask> todo = new ArrayList<>();

    public Files(String modConfigName) {
        this.modConfigName = modConfigName;
    }

    @Override
    public String getIdentifier() {
        return modConfigName;
    }

    protected void addToFiles(String fPath, String content, EntityManager manager) {
        System.out.println(fPath + " being added");
        if (!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        Modification mod = (Modification) manager.createNamedQuery("modifications.config", Modification.class)
                .setParameter("configPath", modConfigName)
                .getSingleResult();
        Original file;
        try {
            file = (Original) manager.createNamedQuery("original.path", Original.class)
                    .setParameter("path", fPath)
                    .getSingleResult();
        } catch (NoResultException ex) {
            file = new Original(fPath);
            manager.persist(file);
        }
        Patch patch = new Patch(mod, file);
        patch.setDiff(content);
        manager.persist(patch);
        manager.getTransaction().commit();
        System.out.println(fPath + " was added");
    }

}
