/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.idrinth.stellaris.modtools.parser;

import com.github.sarxos.winreg.RegistryException;
import de.idrinth.stellaris.modtools.access.DirectoryLookup;
import de.idrinth.stellaris.modtools.model.Mod;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Björn
 */
public class Configuration {
    protected File path;
    protected String name;
    protected String version;
    protected int id;
    private File getWithPrefix(String path, String prefix) throws IOException {
        File file = new File(path);
        if(file.exists()) {
            return file;
        }
        file = new File(prefix+"\\"+path);
        if(file.exists()) {
            return file;
        }
        throw new IOException("No valid path to mod found: "+file.getAbsolutePath());
    }
    public Configuration(File file) throws IOException, RegistryException {
        for(String line:FileUtils.readFileToString(file, "utf-8").split("\\r?\\n\\r?")) {
            if(null != line && line.matches("\\s*[a-z_]+\\s*=\\s*\".*?\"")) {
                String[] parts = line.trim().split("=",2);
                String value = parts[1].trim().replaceAll("^\"|\"$", "");
                switch(parts[0].trim()) {
                    case "archive"://downloaded mod
                        path = getWithPrefix(value, DirectoryLookup.getSteamDir().getAbsolutePath());
                        break;
                    case "remote_file_id"://downloaded mod
                        id = Integer.parseInt(value,10);
                        break;
                    case "name":
                        name = value;
                        break;
                    case "path"://local mod
                        path = getWithPrefix(value, DirectoryLookup.getModDir().getParent());
                        break;
                    case "supported_version":
                        version = value;
                        break;
                    default:
                        //TODO: tags, dependencies, image?
                }
            }
        }
    }

    public void configure(Mod mod) {
        if(id != 0) {
            mod.setId(id);
        }
        if(name != null) {
            mod.setName(name);
        }
        if(path != null) {
            mod.setPath(path.getAbsolutePath());
        } else {
            mod.broken = true;
        }
        if(version != null) {
            mod.setVersion(version);
        }
    }
}
