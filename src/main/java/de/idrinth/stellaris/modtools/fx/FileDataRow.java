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
package de.idrinth.stellaris.modtools.fx;

import de.idrinth.stellaris.modtools.entity.ModFile;
import de.idrinth.stellaris.modtools.entity.Modification;
import de.idrinth.stellaris.modtools.entity.StellarisFile;
import java.util.HashSet;
import java.util.LinkedList;
import com.sksamuel.diffpatch.DiffMatchPatch;

public class FileDataRow {

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

    private boolean isModCovered(Modification m, HashSet<Modification> list) {
        return list.stream().anyMatch((m2) -> (m2.getOverwrite().contains(m)));
    }

    public String getCollisions() {
        String result = "";
        int counter = 0;
        for (Modification m : getColliding()) {
            result = result + "\\n" + m.getName() + " [" + m.getId() + "]";
            counter++;
        }
        return counter > 0 && result.length() > 0 ? result.substring(1) : result;
    }

    private HashSet<Modification> getColliding() {
        HashSet<Modification> collisions = new HashSet<>();
        HashSet<Modification> colliding = new HashSet<>();
        file.getMods().forEach((mf) -> {
            collisions.add(mf.getMod());
        });
        collisions.stream().filter((m) -> (!isModCovered(m, collisions))).forEachOrdered((m) -> {
            colliding.add(m);
        });
        return colliding;
    }
}
