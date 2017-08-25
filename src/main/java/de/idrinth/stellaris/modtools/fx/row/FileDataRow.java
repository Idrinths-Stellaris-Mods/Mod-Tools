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

import de.idrinth.stellaris.modtools.entity.ModFile;
import de.idrinth.stellaris.modtools.entity.Modification;
import de.idrinth.stellaris.modtools.entity.StellarisFile;
import java.util.HashSet;
import java.util.LinkedList;
import com.sksamuel.diffpatch.DiffMatchPatch;

public class FileDataRow extends AbstractDataRow {

    private final StellarisFile file;

    public FileDataRow(StellarisFile file) {
        this.file = file;
    }

    public String getName() {
        return file.getRelativePath();
    }

    public String getPatchable() {
        return String.valueOf(file.isPatchable());
    }

    public String getImportance() {
        if (null == file.getMods() || file.getMods().size() < 2) {
            return "none";
        }
        if (!file.isPatchable()) {
            return "low";
        }
        return getColliding().size() > 1 ? "high" : "medium";
    }

    public String getPatch() {
        if (!file.isPatchable()) {
            return "- not patchable, but unimportant -";
        }
        DiffMatchPatch patcher = new DiffMatchPatch();
        String result = file.getContent();
        HashSet<Modification> colliding = getColliding();
        for (ModFile mf : file.getMods()) {
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
        file.getMods().forEach((mf) -> {
            collisions.add(mf.getMod());
        });
        return collisions;
    }
}
