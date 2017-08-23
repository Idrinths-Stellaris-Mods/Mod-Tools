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
        System.out.println("Mod added: "+config.getName());
        mod.lock();
    }
}
