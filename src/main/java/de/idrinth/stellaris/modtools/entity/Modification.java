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
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import org.hibernate.annotations.NaturalId;

@NamedQueries({
    @NamedQuery(
        name = "modifications",
        query = "select m from Modification m"
    ),
    @NamedQuery(
        name = "modifications.id",
        query = "select m from Modification m where m.id=:id"
    ),
    @NamedQuery(
        name = "modifications.config",
        query = "select m from Modification m where m.configPath=:configPath"
    )
})
@Entity
public class Modification implements Serializable {
    @Id
    @GeneratedValue
    private long aid;
    //basics
    @NaturalId
    protected String configPath;
    @NaturalId
    protected int id;
    protected String name;
    protected String version;
    @Column(columnDefinition="TEXT")
    protected String description;
    //connection
    @OneToMany
    protected Set<Patch> files;
    @ManyToMany
    protected Set<Modification> overwrite;

    public String getName() {
        return name;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
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

    public Set<Patch> getPatches() {
        return files;
    }

    public void setPatches(Set<Patch> files) {
        this.files = files;
    }

    @Override
    public int hashCode() {
        return 18605 + 61 * Objects.hashCode(this.configPath) + this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Modification other = (Modification) obj;
        return this.id == other.id && Objects.equals(this.configPath, other.configPath);
    }

}
