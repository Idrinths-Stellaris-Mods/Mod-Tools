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

public class ModDataRow extends AbstractDataRow{

    private final Modification mod;

    public ModDataRow(Modification mod) {
        this.mod = mod;
    }

    public String getName() {
        return mod.getName();
    }

    public String getVersion() {
        return mod.getVersion();
    }

    public int getId() {
        return mod.getId();
    }

    public String getDescription() {
        return mod.getDescription();
    }

    @Override
    protected HashSet<Modification> getRelatedModifications() {
        HashSet<Modification> collisions = new HashSet<>();
        mod.getFiles().forEach((mf) -> {
            mf.getFile().getMods().forEach((fm) -> {
                collisions.add(fm.getMod());
            });
        });
        return collisions;
    }
}
