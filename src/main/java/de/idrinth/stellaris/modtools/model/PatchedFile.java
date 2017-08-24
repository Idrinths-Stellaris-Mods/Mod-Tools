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

import java.util.HashMap;

public class PatchedFile {
    protected String content;
    protected HashMap<String,Mod> mods;
    protected String file;

    public PatchedFile(String content, HashMap<String,Mod> mods, String file) {
        this.file = file;
        this.content = content;
        this.mods = mods;
    }

    public String getContent() {
        return "<h1>"+file+"</h1>"+getLinks()+"<pre>"+content+"</pre>";
    }

    private String getLinks() {
        String modstring = "<ul>";
        for(Mod mod:mods.values()) {
            modstring = modstring+"<li><a href=\"http://steamcommunity.com/sharedfiles/filedetails/?id="+mod.getId()+"\">"+mod.getName()+"</a></li>";
        }
        return modstring+"</ul>";
    }

    public String getFile() {
        return file;
    }

    public boolean isPatched() {
        return mods.size()>1;
    }

    public String getMods() {
        String modstring = "";
        for(String key:mods.keySet()) {
            modstring = modstring+"\n"+key;
        }
        return modstring.substring(1);
    }

}
