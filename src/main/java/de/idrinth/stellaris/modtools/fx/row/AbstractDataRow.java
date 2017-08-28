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
package de.idrinth.stellaris.modtools.fx.row;

import de.idrinth.stellaris.modtools.entity.Modification;
import java.util.HashSet;

abstract public class AbstractDataRow {
    protected boolean isModCovered(Modification m, HashSet<Modification> list) {
        return list.stream().anyMatch((m2) -> (m2.getOverwrite().contains(m)));
    }
    public String getCollisions() {return "";
        /*String result = "";
        int counter = 0;
        for (Modification m : getColliding()) {
            result = result + "\\n" + m.getName() + " [" + m.getId() + "]";
            counter++;
        }
        return counter > 1 && result.length() > 0 ? result.substring(1) : result;*/
    }
    protected abstract HashSet<Modification> getRelatedModifications();
    protected HashSet<Modification> getColliding() {
        HashSet<Modification> collisions = getRelatedModifications();
        HashSet<Modification> colliding = new HashSet<>();
        collisions.stream().filter((m) -> (!isModCovered(m, collisions))).forEachOrdered((m) -> {
            colliding.add(m);
        });
        return colliding;
    }
}
