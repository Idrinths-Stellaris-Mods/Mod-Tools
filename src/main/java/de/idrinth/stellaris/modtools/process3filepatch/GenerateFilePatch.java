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
package de.idrinth.stellaris.modtools.process3filepatch;

import com.sksamuel.diffpatch.DiffMatchPatch;
import de.idrinth.stellaris.modtools.persistence.entity.Patch;
import de.idrinth.stellaris.modtools.process.ProcessTask;
import de.idrinth.stellaris.modtools.filesystem.FileExtensions;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

class GenerateFilePatch implements ProcessTask {

    private final long id;

    public GenerateFilePatch(long id) {
        this.id = id;
    }

    @Override
    public List<ProcessTask> handle(EntityManager manager) {
        if (!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        Patch patch = (Patch) manager.find(Patch.class, id);
        if (FileExtensions.isPatchable(patch.getFile().getRelativePath())) {
            DiffMatchPatch dmp = new DiffMatchPatch();
            String result = patch.getDiff();
            String original = patch.getFile().getContent();
            patch.setDiff(dmp.patch_toText(dmp.patch_make(original, result)));
        }
        manager.getTransaction().commit();
        return new ArrayList<>();
    }

    @Override
    public String getIdentifier() {
        return String.valueOf(id);
    }
}
