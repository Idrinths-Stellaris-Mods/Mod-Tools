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

import de.idrinth.stellaris.modtools.MainApp;
import de.idrinth.stellaris.modtools.entity.Modification;
import de.idrinth.stellaris.modtools.entity.PatchedFile;
import java.util.Set;

public class FileDataRow extends AbstractDataRow {

    private final String file;

    public FileDataRow(PatchedFile file) {
        this.file = file.getRelativePath();
    }

    public String getName() {
        return file;
    }

    public String getImportance() {
        PatchedFile fileO = (PatchedFile) MainApp.entityManager.createEntityManager().find(PatchedFile.class, file);
        int i = fileO.getImportance();
        return i==0?"low":i==1?"medium":"high";
    }

    public String getPatch() {
        PatchedFile fileO = (PatchedFile) MainApp.entityManager.createEntityManager().find(PatchedFile.class, file);
        return fileO.getContent();
    }

    @Override
    protected Set<Modification> getCollisionList() {
        PatchedFile fileO = (PatchedFile) MainApp.entityManager.createEntityManager().find(PatchedFile.class, file);
        return fileO.getModifications();
    }
}
