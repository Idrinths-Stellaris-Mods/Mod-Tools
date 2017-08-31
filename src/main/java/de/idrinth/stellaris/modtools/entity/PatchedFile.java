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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import org.hibernate.annotations.NaturalId;
@NamedQueries({
    @NamedQuery(
            name = "patched",
            query = "select f from PatchedFile f"
    )
})
@Entity
public class PatchedFile implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    @NaturalId
    @OneToOne(fetch = FetchType.LAZY)
    private Original original;
    @OneToOne(fetch = FetchType.LAZY)
    private LazyText content;
    private int importance;
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Modification> modifications = new HashSet<>();

    public PatchedFile() {
    }

    public PatchedFile(Original original) {
        this.original = original;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Original getOriginal() {
        return original;
    }

    public void setOriginal(Original original) {
        this.original = original;
    }

    public LazyText getContent() {
        return content;
    }

    public void setContent(LazyText content) {
        this.content = content;
    }

    public void setContent(String content) {
        if(null == this.content) {
            throw new IllegalStateException("No LazyText initialized yet");
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

    @Override
    public int hashCode() {
        return 83 * 7 + Objects.hashCode(this.original);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final PatchedFile other = (PatchedFile) obj;
        return Objects.equals(this.original, other.original);
    }
    
}
