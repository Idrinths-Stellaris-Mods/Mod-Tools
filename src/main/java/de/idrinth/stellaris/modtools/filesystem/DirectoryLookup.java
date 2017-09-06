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

import com.github.sarxos.winreg.HKey;
import com.github.sarxos.winreg.RegistryException;
import com.github.sarxos.winreg.WindowsRegistry;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.lang3.SystemUtils;

class DirectoryLookup {

    protected static File modDir;
    protected static File steamDir;

    public static File getModDir() throws IOException {
        if (null == modDir) {
            if(SystemUtils.IS_OS_WINDOWS) {
                modDir = test(new File(SystemUtils.getUserHome() + "\\Documents\\Paradox Interactive\\Stellaris\\mod"));
            } else if(SystemUtils.IS_OS_MAC) {
                modDir = test(new File(SystemUtils.getUserHome() + "/Documents/Paradox Interactive/Stellaris/mod"));
            } else if(SystemUtils.IS_OS_LINUX) {
                modDir = test(new File(SystemUtils.getUserHome() + "/.local/share/Paradox Interactive/Stellaris/mod"));
            }
        }
        return modDir;
    }

    public static File getSteamDir() throws RegistryException, IOException {
        if (null == steamDir) {
            if(SystemUtils.IS_OS_WINDOWS) {
                steamDir = test(new File(WindowsRegistry.getInstance().readString(HKey.HKCU, "Software\\Valve\\Steam", "SteamPath")));
            } else if(SystemUtils.IS_OS_MAC) {
                steamDir = test(new File(SystemUtils.getUserHome()+"/Library/Application Support/Steam"));
            } else if(SystemUtils.IS_OS_LINUX) {
                ArrayList<File> fl = new ArrayList<>();
                fl.add(new File(SystemUtils.getUserHome()+"/.steam/steam"));
                fl.add(new File(SystemUtils.getUserHome()+"/.steam/Steam"));
                fl.add(new File(SystemUtils.getUserHome()+"/.local/share/steam"));
                fl.add(new File(SystemUtils.getUserHome()+"/.local/share/Steam"));
                steamDir = test(fl);
            }
        }
        return steamDir;
    }

    private static File test(File file) throws IOException {
        if (!file.exists() && file.isDirectory()) {
            throw new IOException(file.getAbsolutePath() + " is expected but can't be found.");
        }
        return file;
    }

    private static File test(ArrayList<File> files) throws IOException {
        StringBuilder sb = new StringBuilder();
        for(File file:files) {
            try {
                return test(file);
            } catch(IOException e) {
                sb.append(e.getMessage());
            }
        }
        throw new IOException(sb.toString());
    }
}
