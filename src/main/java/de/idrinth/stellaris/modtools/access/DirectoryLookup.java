package de.idrinth.stellaris.modtools.access;

import com.github.sarxos.winreg.HKey;
import com.github.sarxos.winreg.RegistryException;
import com.github.sarxos.winreg.WindowsRegistry;
import java.io.File;
import java.io.IOException;

public class DirectoryLookup {// assuming windows atm
    protected static File modDir;
    protected static File steamDir;
    public static File getModDir() throws IOException {
        if(null == modDir) {
            modDir = test(new File(System.getProperty("user.home")+"\\Documents\\Paradox Interactive\\Stellaris\\mod"));
        }
        return modDir;
    }
    public static File getSteamDir() throws RegistryException, IOException {
        if(null == steamDir) {
            steamDir = test(new File(WindowsRegistry.getInstance().readString(HKey.HKCU,"Software\\Valve\\Steam", "SteamPath")));
        }
        return steamDir;
    }
    private static File test(File file) throws IOException {
        if(!file.exists() && file.isDirectory()) {
            throw new IOException(file.getAbsolutePath()+" is expected but can't be found");
        }
        return file;
    }
}
