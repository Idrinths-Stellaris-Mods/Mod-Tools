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
package de.idrinth.stellaris.modtools.process2prepatchcleaning;

import de.idrinth.stellaris.modtools.persistence.entity.Colliding;
import de.idrinth.stellaris.modtools.persistence.entity.Original;
import de.idrinth.stellaris.modtools.persistence.entity.Patch;
import de.idrinth.stellaris.modtools.process.ProcessTask;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

class RemoveOverwrittenFilePatch implements ProcessTask {

    private final long id;
    private boolean modified = false;

    public RemoveOverwrittenFilePatch(long id) {
        this.id = id;
    }

    @Override
    public List<ProcessTask> handle(EntityManager manager) {
        if (!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        Original original = (Original) manager.find(Original.class, id);
        ArrayList<Long> ignores = new ArrayList<>();
        original.getPatches().forEach((patch) -> {
            patch.getMod().getOverwrite().forEach((mod) -> {
                ignores.add(mod.getAid());
            });
        });
        ArrayList<Patch> toRemove = new ArrayList<>();
        original.getPatches().stream().filter((patch) -> (ignores.contains(patch.getMod().getAid()))).map((patch) -> {
            toRemove.add(patch);
            return patch;
        }).forEachOrdered((_item) -> {
            modified = true;
        });
        toRemove.stream().map((patch) -> {
            patch.getMod().getPatches().remove(patch);
            return patch;
        }).map((patch) -> {
            original.getPatches().remove(patch);
            return patch;
        }).forEachOrdered((patch) -> {
            manager.remove(patch);
        });
        original.getPatches().forEach((patch1) -> {
            original.getPatches().forEach((patch2) -> {
                if (!patch1.equals(patch2)) {
                    if (null == patch1.getMod().getCollides()) {
                        patch1.getMod().setCollides(new Colliding(patch1.getMod()));
                    }
                    patch1.getMod().getCollides().getModifications().add(patch2.getMod());
                }
            });
        });
        manager.getTransaction().commit();
        ArrayList<ProcessTask> list = new ArrayList<>();
        if(modified) {
            list.add(new RemoveSingleUseFiles(id));
        }
        return list;
    }

    @Override
    public String getIdentifier() {
        return String.valueOf(id);
    }
}
