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
package de.idrinth.stellaris.modtools.gui;

import de.idrinth.stellaris.modtools.persistence.entity.Modification;
import java.util.Set;
import javax.persistence.EntityManager;

abstract class AbstractDataRow {

    protected final EntityManager manager;

    public AbstractDataRow(EntityManager manager) {
        this.manager = manager;
    }

    public String getCollisions() {
        Set<Modification> list = getCollisionList();
        String output = "";
        if (list.size() < 1) {
            return output;
        }
        for (Modification collision : list) {
            output = output + "\n" + collision.getName() + " [" + collision.getId() + "]";
        }
        return output.substring(1);
    }

    protected abstract Set<Modification> getCollisionList();
}
