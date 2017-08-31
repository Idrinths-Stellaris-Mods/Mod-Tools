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
package de.idrinth.stellaris.modtools.fx.row;

import de.idrinth.stellaris.modtools.entity.Modification;
import de.idrinth.stellaris.modtools.entity.PatchedFile;
import java.util.Set;
import javax.persistence.EntityManager;

public class FileDataRow extends AbstractDataRow {

    private final long id;

    public FileDataRow(PatchedFile file, EntityManager manager) {
        super(manager);
        this.id = file.getId();
    }

    public String getName() {
        PatchedFile fileO = (PatchedFile) manager.find(PatchedFile.class, id);
        return fileO.getOriginal().getRelativePath();
    }

    public String getImportance() {
        PatchedFile fileO = (PatchedFile) manager.find(PatchedFile.class, id);
        int i = fileO.getImportance();
        return i==0?"low":i==1?"medium":"high";
    }

    public String getPatchable() {
        PatchedFile fileO = (PatchedFile) manager.find(PatchedFile.class, id);
        if(fileO.isPatchable()) {
            return "auto";
        }
        return fileO.isPatchableExt()?"manually":"no";
    }

    public String getPatch() {
        PatchedFile fileO = (PatchedFile) manager.find(PatchedFile.class, id);
        return "<pre>"+fileO.getContent().toString()+"</pre>";
    }

    @Override
    protected Set<Modification> getCollisionList() {
        PatchedFile fileO = (PatchedFile) manager.find(PatchedFile.class, id);
        return fileO.getModifications();
    }
}
