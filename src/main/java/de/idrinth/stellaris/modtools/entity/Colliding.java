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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Colliding implements Serializable {
    @Id
    @OneToOne
    private Modification modification;
    @OneToMany(fetch = FetchType.LAZY)
    private Set<Modification> modifications = new HashSet<>();

    public Colliding() {
    }

    public Colliding(Modification modification) {
        this.modification = modification;
    }

    public Modification getModification() {
        return modification;
    }

    public void setModification(Modification modification) {
        this.modification = modification;
    }

    public Set<Modification> getModifications() {
        return modifications;
    }

    public void setModifications(Set<Modification> modifications) {
        this.modifications = modifications;
    }

    @Override
    public int hashCode() {
        return 67 * 3 + Objects.hashCode(this.modification);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Colliding other = (Colliding) obj;
        return Objects.equals(this.modification, other.modification);
    }
    
}
