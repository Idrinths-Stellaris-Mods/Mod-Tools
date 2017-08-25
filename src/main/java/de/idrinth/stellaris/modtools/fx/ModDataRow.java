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
package de.idrinth.stellaris.modtools.fx;

import de.idrinth.stellaris.modtools.entity.ModFile;
import de.idrinth.stellaris.modtools.entity.Modification;
import java.util.HashSet;


public class ModDataRow {
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

    private boolean isModCovered(Modification m,HashSet<Modification> list) {
        if(m == mod) {
            return true;
        }
        return list.stream().anyMatch((m2) -> (m2.getOverwrite().contains(m)));
    }
    
    public String getCollisions() {
        HashSet<Modification> collisions = new HashSet<>();
        mod.getFiles().forEach((mf) -> {
            mf.getFile().getMods().forEach((fm) -> {
                collisions.add(fm.getMod());
            });
        });
        String result = "";
        int counter = 0;
        for(Modification m:collisions) {
            if(!isModCovered(m,collisions)) {
                result = result+"\\n"+m.getName()+" ["+m.getId()+"]";
                counter++;
            }
        }
        return counter>0&&result.length()>0?result.substring(1):result;
    }
}
