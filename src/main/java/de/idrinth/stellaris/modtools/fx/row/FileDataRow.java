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
package de.idrinth.stellaris.modtools.fx.row;

import de.idrinth.stellaris.modtools.entity.Patch;
import de.idrinth.stellaris.modtools.entity.Modification;
import de.idrinth.stellaris.modtools.entity.Original;
import java.util.HashSet;
import java.util.LinkedList;
import com.sksamuel.diffpatch.DiffMatchPatch;
import de.idrinth.stellaris.modtools.MainApp;

public class FileDataRow extends AbstractDataRow {

    private final String file;

    public FileDataRow(Original file) {
        this.file = file.getRelativePath();
    }

    public String getName() {
        return file;
    }

    public String getImportance() {
        Original fileO = (Original) MainApp.entityManager.createEntityManager().find(Original.class, file);
        if (null == fileO.getPatches() || fileO.getPatches().size() < 2) {
            return "none";
        }
        if (file.endsWith(".txt")||file.endsWith(".yml")) {
            return getColliding().size() > 1 ? "high" : "medium";
        }
        return "low";
    }

    public String getPatch() {
        if (!(file.endsWith(".txt")||file.endsWith(".yml"))) {
            return "- not patchable, but unimportant -";
        }
        DiffMatchPatch patcher = new DiffMatchPatch();
        Original fileO = (Original) MainApp.entityManager.createEntityManager().find(Original.class, file);
        String result = fileO.getContent();
        HashSet<Modification> colliding = getColliding();
        for (Patch mf : fileO.getPatches()) {
            if (colliding.contains(mf.getMod())) {
                Object[] data = patcher.patch_apply(
                        new LinkedList<>(patcher.patch_fromText(mf.getDiff())),
                        result
                );
                result = (String) data[0];
                for (boolean success : (boolean[]) data[1]) {
                    if (!success) {
                        return "failed to automatically patch";
                    }
                }
            }
        }
        return result;
    }

    @Override
    protected HashSet<Modification> getRelatedModifications() {
        HashSet<Modification> collisions = new HashSet<>();
        Original fileO = (Original) MainApp.entityManager.createEntityManager().find(Original.class, file);
        fileO.getPatches().forEach((mf) -> {
            collisions.add(mf.getMod());
        });
        return collisions;
    }
}
