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
package de.idrinth.stellaris.modtools.model;

import de.idrinth.stellaris.modtools.access.StellarisFile;
import java.util.HashMap;
import name.fraser.neil.plaintext.diff_match_patch;

public class ModCollection {
    protected HashMap<String,Collision> files = new HashMap<>();
    protected HashMap<String,Mod> mods = new HashMap<>();
    protected diff_match_patch patcher = new diff_match_patch();

    public void add(Mod mod) {
        String key = mod.getId() > 0?String.valueOf(mod.getId()):mod.getName();
        if(mods.containsKey(key) && mods.get(key).broken) {
            return;
        }
        mods.put(key, mod);
        mods.get(key).getFiles().stream().map((file) -> {
            if(!files.containsKey(file)) {
                files.put(file, new Collision(StellarisFile.get(file),this, file, patcher));
            }
            return file;
        }).forEachOrdered((file) -> {
            files.get(file).add(mod.getFileContent(file), key);
        });
    }

    public HashMap<String, Collision> getFiles() {
        return files;
    }

    public HashMap<String, Mod> getMods() {
        return mods;
    }
}
