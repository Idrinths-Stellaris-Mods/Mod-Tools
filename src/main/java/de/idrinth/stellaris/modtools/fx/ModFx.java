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
package de.idrinth.stellaris.modtools.fx;

import de.idrinth.stellaris.modtools.model.Mod;
import javafx.beans.property.SimpleStringProperty;

public class ModFx {

    protected SimpleStringProperty description;
    protected SimpleStringProperty id;
    protected SimpleStringProperty name;
    protected SimpleStringProperty version;

    public ModFx(Mod mod) {
        id = new SimpleStringProperty(String.valueOf(mod.getId()));
        name = new SimpleStringProperty(mod.getName());
        version = new SimpleStringProperty(mod.getVersion());
        description = new SimpleStringProperty(mod.getDescription());
    }

    public String getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public String getVersion() {
        return version.get();
    }

    public String getDescription() {
        return description.get();
    }

}
