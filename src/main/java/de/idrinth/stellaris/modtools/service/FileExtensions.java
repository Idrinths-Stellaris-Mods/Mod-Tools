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
package de.idrinth.stellaris.modtools.service;

public class FileExtensions {
    private static final String[] PATCH = ".txt,.yml,.asset,.csv,.gfx".split(",");
    private static final String[] REPLACE = ".wav,.ogg,.ods,.dds,.bmp,.png,.psd,.jpg,.ani,.cur,.ttf,.fnt,.tga,.otf,.shader,.fxh,.anim,.mesh,.gui".split(",");
    public static boolean isPatchable(String filename) {
        return isInList(PATCH,filename);
    }
    public static boolean isReplaceable(String filename) {
        return isInList(REPLACE,filename);
    }
    public static String[] getPatchable() {
        return PATCH;
    }
    public static String[] getReplaceable() {
        return REPLACE;
    }
    private static boolean isInList(String[] list, String filename) {        
        for (String ext : list) {
            if (filename.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
}
