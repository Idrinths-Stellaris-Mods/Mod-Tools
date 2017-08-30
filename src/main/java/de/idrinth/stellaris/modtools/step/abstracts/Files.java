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
package de.idrinth.stellaris.modtools.step.abstracts;

import de.idrinth.stellaris.modtools.access.Queue;
import de.idrinth.stellaris.modtools.entity.Modification;
import de.idrinth.stellaris.modtools.entity.Original;
import de.idrinth.stellaris.modtools.entity.Patch;
import de.idrinth.stellaris.modtools.step.OriginalFileFiller;
import javax.persistence.EntityManager;

abstract public class Files extends TaskList {
    protected final String modConfigName;

    public Files(Queue queue, String modConfigName) {
        super(queue);
        this.modConfigName = modConfigName;
    }
    @Override
    protected String getIdentifier() {
        return modConfigName;
    }

    protected void addToFiles(String fPath, String content) {
        fPath = fPath.replace("\\", "/");
        System.out.println(fPath+" being added");
        EntityManager manager = getEntityManager();
        if(!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        Modification mod = (Modification) manager.createNamedQuery("modifications.config", Modification.class)
                .setParameter("configPath", modConfigName)
                .getSingleResult();
        if(null == mod) {
            mod = new Modification(modConfigName,0);
        }
        Original file = (Original) manager.find(Original.class, fPath);
        if(null == file) {
            file = new Original(fPath);
            tasks.add(new OriginalFileFiller(fPath));
            manager.persist(file);
        }
        Patch patch = new Patch(mod,file);
        patch.setDiff(content);
        mod.getPatches().add(patch);
        file.getPatches().add(patch);
        manager.persist(patch);
        manager.getTransaction().commit();
    }

}
