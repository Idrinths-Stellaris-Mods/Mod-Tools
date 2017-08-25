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
package de.idrinth.stellaris.modtools.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class File implements Serializable {
    //original
    protected String content;
    protected boolean patchable=true;
    @Id
    protected String relativePath;
    //connection
    @OneToMany
    protected Set<ModFile> mods;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isPatchable() {
        return patchable;
    }

    public void setPatchable(boolean patchable) {
        this.patchable = patchable;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String path) {
        this.relativePath = path;
    }

    public Set<ModFile> getMods() {
        return mods;
    }

    public void setMods(Set<ModFile> mods) {
        this.mods = mods;
    }
    
}
