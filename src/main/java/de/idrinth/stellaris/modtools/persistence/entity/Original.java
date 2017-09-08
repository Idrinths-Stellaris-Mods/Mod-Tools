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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@NamedQueries({
    @NamedQuery(
            name = "originals",
            query = "select f from Original f"
    )
    ,
    @NamedQuery(
            name = "original.path",
            query = "select f from Original f where f.relativePath=:path"
    )
})
@Entity
public class Original extends EntityCompareAndHash {

    @Id
    @GeneratedValue
    private long aid;
    //original
    @OneToOne(fetch = FetchType.LAZY)
    @Cascade({CascadeType.DELETE,CascadeType.PERSIST})
    private LazyText content;
    protected String relativePath;
    //connection
    @OneToMany(fetch = FetchType.LAZY, mappedBy="file")
    @Cascade({CascadeType.DELETE})
    protected Set<Patch> patches = new HashSet<>();

    public Original() {
    }

    public Original(String relativePath) {
        this.relativePath = relativePath;
    }

    @Override
    public long getAid() {
        return aid;
    }

    @Override
    public void setAid(long aid) {
        this.aid = aid;
    }

    public String getContent() {
        if(null == content) {
            content = new LazyText();
        }
        return content.getText();
    }

    public void setContent(String content) {
        if(null == this.content) {
            this.content = new LazyText();
        }
        this.content.setText(content);
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public Set<Patch> getPatches() {
        return patches;
    }

    public void setPatches(Set<Patch> patches) {
        this.patches = patches;
    }
}
