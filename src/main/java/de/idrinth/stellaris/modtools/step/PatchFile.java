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

import de.idrinth.stellaris.modtools.entity.Original;
import de.idrinth.stellaris.modtools.step.abstracts.TaskList;
import javax.persistence.EntityManager;

public class PatchFile extends TaskList {
    private String file;
    public PatchFile(String file) {
        super(null);
    }

    @Override
    protected void fill() {
        EntityManager manager = getEntityManager();
        Original original = (Original) manager.find(Original.class, file);
        if(original.getPatches().size()<2) {
            return;
        }
        if(!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        String data = original.getContent();
       // manager.persist(mod);
        manager.getTransaction().commit();
    }
    
}
