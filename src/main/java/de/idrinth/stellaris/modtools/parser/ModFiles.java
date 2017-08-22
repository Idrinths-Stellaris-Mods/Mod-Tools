package de.idrinth.stellaris.modtools.parser;

import com.github.sarxos.winreg.HKey;
import com.github.sarxos.winreg.RegistryException;
import com.github.sarxos.winreg.WindowsRegistry;
import de.idrinth.stellaris.modtools.model.Mod;
import de.idrinth.stellaris.modtools.model.ModCollection;
import de.idrinth.stellaris.modtools.filter.FileExt;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class ModFiles {
    protected File modDir;
    protected File steamDir;
    protected File getModDir() throws IOException {
        if(null == modDir) {// assuming windows
            File file = new File(System.getProperty("user.home")+"\\Documents\\Paradox Interactive\\Stellaris\\mod");
            if(!file.exists()) {
               throw new IOException("No mod directory has been found");
            }
            modDir = file;
        }
        return modDir;
    }
    protected File getSteamDir() throws IOException, RegistryException {
        if(null == steamDir) {// assuming windows
            steamDir = new File(WindowsRegistry.getInstance().readString(HKey.HKCU,"Software\\Valve\\Steam", "SteamPath"));
        }
        return steamDir;
    }
    public ModCollection get(ModCollection list) throws IOException, RegistryException {
        for(File mod:getModDir().listFiles(new FileExt("mod"))) {
            parse(mod, list);
        }
        return list;
    }
    protected void parse(File config, ModCollection list) throws IOException, RegistryException {
        Mod mod = new Mod(list);
        String[] conf = FileUtils.readFileToString(config).split("/\n/");
        for(String line:conf) {
            line = line.trim();
            if(!"}".equals(line) && !line.endsWith("{") && line.matches("/=/")) {
                String[] parts = line.split("/=/");
                String value = parts[1].trim();
                switch(parts[0].trim()) {
                    case "archive"://downloaded mod
                        mod.setPath(getSteamDir().getAbsolutePath()+"/"+value);
                        break;
                    case "remote_file_id"://downloaded mod
                        mod.setId(Integer.parseInt(value,10));
                        break;
                    case "name":
                        mod.setName(value);
                        break;
                    case "path"://local mod
                        mod.setPath(getModDir().getAbsolutePath()+"/"+value);
                        break;
                    case "supported_version":
                        mod.setVersion(value);
                        break;
                    default:
                        //TODO: tags, dependencies, image?
                }
            }
        }
        mod.lock();
    }
}
