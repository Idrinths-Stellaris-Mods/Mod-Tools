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
package de.idrinth.stellaris.modtools.entity;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
@NamedQueries({
    @NamedQuery(
	name = "all_modifications",
	query = "select m from Modification m"
    )
})
@Entity
public class Modification implements Serializable {
    @Id
    protected String relativePath;
    //basics
    protected String name;
    protected String version;
    //Remote only
    protected int id = 0;
    protected String description;
    //connection
    @OneToMany
    protected Set<ModFile> files;
    @ManyToMany
    protected Set<Modification> overwrite;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public Set<Modification> getOverwrite() {
        return overwrite;
    }

    public void setOverwrite(Set<Modification> overwrite) {
        this.overwrite = overwrite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ModFile> getFiles() {
        return files;
    }

    public void setFiles(Set<ModFile> files) {
        this.files = files;
    }
    
}
