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

import de.idrinth.stellaris.modtools.entity.Colliding;
import de.idrinth.stellaris.modtools.entity.Original;
import de.idrinth.stellaris.modtools.step.abstracts.TaskList;
import java.util.ArrayList;
import javax.persistence.EntityManager;

public class RemoveOverwrittenFilePatch extends TaskList {
    private final String file;
    public RemoveOverwrittenFilePatch(String file) {
        super(null);
        this.file = file;
    }
    @Override
    protected void fill() {
        try{
        EntityManager manager = getEntityManager();
        if(!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        Original original = (Original) manager.find(Original.class, file);
        ArrayList<Long> ignores = new ArrayList<>();
        original.getPatches().forEach((patch) -> {
            patch.getMod().getOverwrite().forEach((mod) -> {
                ignores.add(mod.getAid());
            });
        });
        original.getPatches().forEach((patch) -> {
            if(ignores.contains(patch.getMod().getAid())) {
                patch.getMod().getPatches().remove(patch);
                original.getPatches().remove(patch);
                manager.remove(patch);
            }
        });
        original.getPatches().forEach((patch1) -> {
            original.getPatches().forEach((patch2) -> {
                if(!patch1.equals(patch2)) {
                    if(null == patch1.getMod().getCollides()) {
                        patch1.getMod().setCollides(new Colliding(patch1.getMod()));
                    }
                    patch1.getMod().getCollides().getModifications().add(patch2.getMod());
                }
            });
        });
        manager.getTransaction().commit();
        } catch(Throwable any) {
            System.err.print(any);
        }
    }

    @Override
    protected String getIdentifier() {
        return file;
    }
}
