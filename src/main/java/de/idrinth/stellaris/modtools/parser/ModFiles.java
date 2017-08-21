package de.idrinth.stellaris.modtools.parser;

import de.idrinth.stellaris.modtools.entity.Mod;
import de.idrinth.stellaris.modtools.entity.ModList;
import de.idrinth.stellaris.modtools.filter.FileExt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;

public class ModFiles {
    protected File modDir;
    protected ModList list = new ModList();
    protected java.io.File getModDir() throws IOException {
        if(null == modDir) {// windows
            File file = new File(System.getProperty("user.home")+"\\Documents\\Paradox Interactive\\Stellaris\\mod");
            if(!file.exists()) {// linux
                file = new File(System.getProperty("user.home")+"/.local/share/Paradox Interactive/Stellaris/mod");
                if(!file.exists()) {// mac
                    file = new File(System.getProperty("user.home")+"/Documents/Paradox Interactive/Stellaris/mod");
                     if(!file.exists()) {
                        throw new IOException("No mod directory has been found");
                    }
                }
            }
            modDir = file;
        }
        return modDir;
    }
    public ModList get() throws IOException {
        list.clear();
        for(File mod:getModDir().listFiles(new FileExt("mod"))) {
            list.add(parse(mod));
        }
        return list;
    }
    protected Mod parse(File config) throws IOException {
        Mod mod = new Mod(list);
        String[] conf = FileUtils.readFileToString(config).split("/\n/");
        ArrayList<String> tags = new ArrayList<>();
        for(String line:conf) {
            line = line.trim();
            if(!line.matches("/=/")) {
                tags.add(line);
            } else if(!line.equals("}") && !line.endsWith("{")) {
                String[] parts = line.split("/=/");
                String value = parts[1].trim();
                switch(parts[0].trim()) {
                    case "archive":
                        mod.setArchive(value);
                        break;
                    case "remote_file_id":
                        mod.setId(Integer.parseInt(value,10));
                        break;
                    case "name":
                        mod.setName(value);
                        break;
                    case "path":
                        mod.setPath(getModDir().getAbsolutePath()+"/"+value);
                        break;
                    case "supported_version":
                        mod.setVersion(value);
                }
            }
        }
        String[] sL = new String[0];
        mod.setTags(tags.toArray(sL));
        mod.lock();
        return mod;
    }
}
