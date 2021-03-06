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
package de.idrinth.stellaris.modtools.persistence.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@NamedQueries({
    @NamedQuery(
            name = "modifications",
            query = "select m from Modification m"
    )
    ,
    @NamedQuery(
            name = "modifications.id",
            query = "select m from Modification m where m.id=:id"
    )
    ,
    @NamedQuery(
            name = "modifications.config",
            query = "select m from Modification m where m.configPath=:configPath"
    )
})
@Entity
public class Modification extends EntityCompareAndHash {

    @Id
    @GeneratedValue
    private long aid;
    //basics
    protected String configPath;
    protected int id;
    protected String name;
    protected String version;
    @OneToOne(fetch = FetchType.LAZY)
    @Cascade({CascadeType.DELETE,CascadeType.PERSIST})
    private LazyText description;
    //connection
    @OneToMany(fetch = FetchType.LAZY, mappedBy="mod")
    protected Set<Patch> patches = new HashSet<>();
    @ManyToMany(fetch = FetchType.LAZY)
    protected Set<Modification> overwrite = new HashSet<>();
    @OneToOne(fetch = FetchType.LAZY)
    @Cascade({CascadeType.ALL})
    protected Colliding collides = new Colliding();

    public Modification() {
    }

    public Modification(String configPath, int id) {
        this.configPath = configPath;
        this.id = id;
    }

    @Override
    public long getAid() {
        return aid;
    }

    @Override
    public void setAid(long aid) {
        this.aid = aid;
    }

    public Set<Patch> getFiles() {
        return patches;
    }

    public void setFiles(Set<Patch> files) {
        this.patches = files;
    }

    public Colliding getCollides() {
        return collides;
    }

    public void setCollides(Colliding collides) {
        this.collides = collides;
    }

    public String getName() {
        return name;
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
        if(null == description) {
            description = new LazyText();
        }
        return description.getText();
    }

    public void setDescription(String description) {
        if(null == this.description) {
            this.description = new LazyText();
        }
        this.description.setText(description);
    }

    public Set<Patch> getPatches() {
        return patches;
    }

    public void setPatches(Set<Patch> files) {
        this.patches = files;
    }
}
