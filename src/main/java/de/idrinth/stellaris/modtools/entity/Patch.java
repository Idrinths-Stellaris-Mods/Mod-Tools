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
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
@NamedQueries({
    @NamedQuery(
            name = "patch.any",
            query = "select f from Patch f"
    )
})
@Entity
public class Patch implements Serializable {

    @Id
    @GeneratedValue
    protected long id;
    @ManyToOne(fetch = FetchType.LAZY)
    protected Modification mod;
    @ManyToOne(fetch = FetchType.LAZY)
    protected Original file;
    @OneToOne(fetch = FetchType.LAZY)
    @Cascade({CascadeType.DELETE})
    private LazyText diff;

    public Patch() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Patch(Modification mod, Original file) {
        this.mod = mod;
        this.file = file;
    }

    public Modification getMod() {
        return mod;
    }

    public void setMod(Modification mod) {
        this.mod = mod;
    }

    public Original getFile() {
        return file;
    }

    public void setFile(Original file) {
        this.file = file;
    }

    public LazyText getDiff() {
        return diff;
    }

    public void setDiff(LazyText diff) {
        this.diff = diff;
    }

    public void setDiff(String diff) {
        if(null == this.diff) {
            throw new IllegalStateException("No LazyText initialized yet");
        }
        this.diff.setText(diff);
    }

    @Override
    public int hashCode() {
        return 2023 + 17 * Objects.hashCode(this.mod) + Objects.hashCode(this.file);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Patch other = (Patch) obj;
        return this.id == other.id || (Objects.equals(this.mod, other.mod) && Objects.equals(this.file, other.file));
    }

}
