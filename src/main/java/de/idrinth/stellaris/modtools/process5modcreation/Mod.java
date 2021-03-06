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
package de.idrinth.stellaris.modtools.process5modcreation;

import de.idrinth.stellaris.modtools.filesystem.FileSystemLocation;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

class Mod {

    private final String filename;
    private final ModLine name = new SingleValue();
    private final ModLine path = new SingleValue();
    private final ModLine dependencies = new MultiValue();
    private final ModLine supported_version = new SingleValue();
    private final ModLine tags = new MultiValue();
    private final FileSystemLocation modDir;

    public Mod(String filename, String modname, FileSystemLocation modDir) {
        addVersionValue("1.0.*");
        addTagValue("Merge");
        path.addValue(modDir.get() + "/" + filename + ".zip");
        name.addValue(modname);
        this.filename = filename;
        this.modDir = modDir;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Field field : this.getClass().getDeclaredFields()) {
            if (ModLine.class.isAssignableFrom(field.getType())) {
                try {
                    sb.append(field.getName());
                    sb.append(" = ");
                    sb.append(field.get(this).toString());
                    sb.append("\n");
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(Mod.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return sb.toString();
    }

    public final void addNameValue(String value) {
        name.addValue(value);
    }

    public final void addVersionValue(String value) {
        supported_version.addValue(value);
    }

    public final String getPathValue() throws IOException {
        return modDir.get() + "/" + filename;
    }

    public final void addDepedencyValue(String value) {
        dependencies.addValue(value);
    }

    public final void addTagValue(String value) {
        tags.addValue(value);
    }
}
