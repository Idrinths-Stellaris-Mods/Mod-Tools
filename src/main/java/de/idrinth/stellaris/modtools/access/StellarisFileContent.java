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
package de.idrinth.stellaris.modtools.access;

import com.github.sarxos.winreg.RegistryException;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class StellarisFileContent {

    public static String get(String relativePath) {
        try {
            return FileUtils.readFileToString(
                    new File(
                            DirectoryLookup.getSteamDir().getAbsolutePath()
                            + "SteamApps/common/Stellaris/"
                            + relativePath
                    ),
                    "utf-8"
            );
        } catch (IOException | RegistryException exception) {
            System.out.println(exception.getLocalizedMessage());
            return "";
        }
    }
}
