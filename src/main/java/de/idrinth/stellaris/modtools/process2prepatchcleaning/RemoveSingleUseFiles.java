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
package de.idrinth.stellaris.modtools.process2prepatchcleaning;

import de.idrinth.stellaris.modtools.entity.Original;
import de.idrinth.stellaris.modtools.entity.Patch;
import de.idrinth.stellaris.modtools.process.ProcessTask;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

public class RemoveSingleUseFiles implements ProcessTask {

    private final long id;

    public RemoveSingleUseFiles(long id) {
        this.id = id;
    }

    @Override
    public List<ProcessTask> handle(EntityManager manager) {
        if (!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        ArrayList<ProcessTask> ll = new ArrayList<>();
        Original original = (Original) manager.find(Original.class, id);
        if(original.getPatches().size()>1) {
            ll.add(new RemoveOverwrittenFilePatch(id));
        } else {
            original.getPatches().stream().map((patch) -> {
                patch.getMod().getPatches().remove(patch);
                return patch;
            }).map((patch) -> {
                original.getPatches().remove(patch);
                return patch;
            }).forEachOrdered((patch) -> {
                manager.remove(patch);
            });
            manager.remove(original);
        }
        manager.getTransaction().commit();
        return ll;
    }

    @Override
    public String getIdentifier() {
        return String.valueOf(id);
    }
}
