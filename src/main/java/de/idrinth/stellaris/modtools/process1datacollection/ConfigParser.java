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
package de.idrinth.stellaris.modtools.process1datacollection;

import com.github.sarxos.winreg.RegistryException;
import de.idrinth.stellaris.modtools.persistence.entity.Modification;
import de.idrinth.stellaris.modtools.filesystem.FileSystemLocation;
import java.io.File;
import java.io.IOException;
import de.idrinth.stellaris.modtools.process.ProcessTask;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.apache.commons.io.FileUtils;

class ConfigParser implements ProcessTask {

    private final File configuration;
    private final ArrayList<ProcessTask> todo = new ArrayList<>();
    private final FileSystemLocation modDir;
    private final FileSystemLocation steamDir;

    public ConfigParser(File configuration, FileSystemLocation modDir, FileSystemLocation steamDir) {
        this.configuration = configuration;
        this.modDir = modDir;
        this.steamDir = steamDir;
    }

    protected File getWithPrefix(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            return file;
        }
        if (!file.isAbsolute()) {
            file = new File(modDir.get().getParent() + "/" + path);
            if (file.exists()) {
                return file;
            }
        }
        throw new IOException("No valid path to mod found: " + file.getAbsolutePath());
    }

    private ProcessTask handlePath(String path) throws IOException, RegistryException {
        File file = getWithPrefix(path);
        if (path.endsWith(".zip")) {
            return new ZipContentParser(configuration.getName(), file, steamDir);
        }
        return new FileSystemParser(configuration.getName(), file, steamDir);
    }

    private void persist(Modification mod, EntityManager manager) {
        mod.setConfigPath(configuration.getName());
        if (null == mod.getCollides().getModification()) {
            mod.getCollides().setModification(mod);
        }
        if (!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        manager.persist(mod);
        manager.getTransaction().commit();
    }

    private void handleLine(Modification mod, String key, String value) throws RegistryException, IOException {
        System.out.println(key + " is " + value);
        switch (key) {
            case "archive":
                todo.add(handlePath(value));
                break;
            case "remote_file_id":
                mod.setId(Integer.parseInt(value, 10));
                todo.add(new RemoteModParser(mod.getId()));
                break;
            case "name":
                mod.setName(value);
                break;
            case "path":
                todo.add(handlePath(value));
                break;
            case "supported_version":
                mod.setVersion(value);
                break;
            case "picture":
                // maybe interesting later?
                break;
            case "replace_path":
            // something like a dependency?
            default:
            //TODO: tags, dependencies?
        }
    }

    @Override
    public String getIdentifier() {
        return configuration.getName();
    }

    @Override
    public List<ProcessTask> handle(EntityManager manager) {
        try {
            if (configuration.exists()) {
                Modification mod = new Modification();
                for (String line : FileUtils.readFileToString(configuration, "utf-8").split("\\r?\\n\\r?")) {
                    if (null != line && line.matches("\\s*[a-z_]+\\s*=\\s*\".*?\"")) {
                        String[] parts = line.trim().split("=", 2);
                        handleLine(mod, parts[0].trim(), parts[1].trim().replaceAll("^\"|\"$", ""));
                    }
                }
                persist(mod, manager);
            }
        } catch (RegistryException | NumberFormatException | IOException ex) {
            Logger.getLogger(ConfigParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return todo;
    }
}
