package de.idrinth.stellaris.modtools.access;

import com.github.sarxos.winreg.RegistryException;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class StellarisFile {
    public static String get(String relativePath) {
        try {
            return FileUtils.readFileToString(new File(
                DirectoryLookup.getSteamDir()+
                        "\\SteamApps\\common\\Stellaris\\"+
                        relativePath
                ),
                "utf-8"
            );
        } catch(IOException|RegistryException exception) {
            return "";
        }
    }
}
