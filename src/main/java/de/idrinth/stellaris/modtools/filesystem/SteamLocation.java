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
package de.idrinth.stellaris.modtools.filesystem;

import com.github.sarxos.winreg.RegistryException;
import java.io.File;
import java.io.IOException;

public class SteamLocation implements FileSystemLocation {
    private final File directory;

    public SteamLocation() throws DirectoryNotFoundException {
        try {
            this.directory = DirectoryLookup.getSteamDir();
        } catch (RegistryException | IOException ex) {
            throw new DirectoryNotFoundException("Steam Directory not avaible",ex);
        }
    }

    @Override
    public File get() {
        return directory;
    }
}
