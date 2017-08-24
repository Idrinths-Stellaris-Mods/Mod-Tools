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
package de.idrinth.stellaris.modtools.parser;

import com.github.sarxos.winreg.RegistryException;
import de.idrinth.stellaris.modtools.access.DirectoryLookup;
import de.idrinth.stellaris.modtools.model.Mod;
import de.idrinth.stellaris.modtools.model.ModCollection;
import de.idrinth.stellaris.modtools.filter.FileExt;
import java.io.File;
import java.io.IOException;

public class ModFiles {
    public ModCollection get(ModCollection list) throws IOException, RegistryException {
        for(File mod:DirectoryLookup.getModDir().listFiles(new FileExt("mod"))) {
            parse(mod, list);
        }
        return list;
    }
    protected void parse(File config, ModCollection list) throws RegistryException {
        Mod mod = new Mod(list);
        System.out.println("Adding Mod: "+config.getName());
        try {
            new Configuration(config).configure(mod);
        } catch(IOException exception) {
            System.out.println(exception.getLocalizedMessage());
            mod.broken = true;
        }
        if(mod.broken) {
            mod.setName(config.getName());
        }
        System.out.println("Mod added: "+config.getName());
        mod.lock();
    }
}
