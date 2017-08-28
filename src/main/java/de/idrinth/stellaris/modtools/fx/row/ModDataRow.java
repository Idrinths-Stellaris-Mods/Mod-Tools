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
import java.util.Set;
import javax.persistence.EntityManager;

public class ModDataRow extends AbstractDataRow{

    private final long mod;

    public ModDataRow(Modification mod, EntityManager manager) {
        super(manager);
        this.mod = mod.getAid();
    }

    public String getName() {
        Modification modO = (Modification) manager.find(Modification.class, mod);
        return modO.getName();
    }

    public String getVersion() {
        Modification modO = (Modification) manager.find(Modification.class, mod);
        return modO.getVersion();
    }

    public int getId() {
        Modification modO = (Modification) manager.find(Modification.class, mod);
        return modO.getId();
    }

    public String getDescription() {
        Modification modO = (Modification) manager.find(Modification.class, mod);
        return modO.getDescription();
    }

    @Override
    protected Set<Modification> getCollisionList() {
        Modification m = (Modification) manager.find(Modification.class, mod);
        return m.getCollides();
    }
}
