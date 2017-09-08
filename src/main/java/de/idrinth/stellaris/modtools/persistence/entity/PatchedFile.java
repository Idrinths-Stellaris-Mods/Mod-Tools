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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@NamedQueries({
    @NamedQuery(
            name = "patched",
            query = "select f from PatchedFile f"
    )
    ,
    @NamedQuery(
            name = "patched.able",
            query = "select f from PatchedFile f where f.patchable=true"
    )
})
@Entity
public class PatchedFile extends EntityCompareAndHash {

    @Id
    @GeneratedValue
    private long aid;
    @OneToOne(fetch = FetchType.LAZY)
    private Original original;
    @OneToOne(fetch = FetchType.LAZY)
    @Cascade({CascadeType.DELETE,CascadeType.PERSIST})
    private LazyText content;
    private int importance;
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Modification> modifications = new HashSet<>();
    private boolean patchable;
    private boolean patchableExt;

    @Override
    public long getAid() {
        return aid;
    }

    @Override
    public void setAid(long aid) {
        this.aid = aid;
    }

    public boolean isPatchable() {
        return patchable;
    }

    public void setPatchable(boolean patchable) {
        this.patchable = patchable;
    }

    public boolean isPatchableExt() {
        return patchableExt;
    }

    public void setPatchableExt(boolean patchableExt) {
        this.patchableExt = patchableExt;
    }

    public PatchedFile() {
    }

    public PatchedFile(Original original) {
        this.original = original;
    }

    public Original getOriginal() {
        return original;
    }

    public void setOriginal(Original original) {
        this.original = original;
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

    public Set<Modification> getModifications() {
        return modifications;
    }

    public void setModifications(Set<Modification> modifications) {
        this.modifications = modifications;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

}
