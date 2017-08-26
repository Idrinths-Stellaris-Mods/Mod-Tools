/*
 * Copyright (C) 2017 Idrinth
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
package de.idrinth.stellaris.modtools.step;

import com.github.sarxos.winreg.RegistryException;
import de.idrinth.stellaris.modtools.MainApp;
import de.idrinth.stellaris.modtools.access.DirectoryLookup;
import de.idrinth.stellaris.modtools.entity.Modification;
import java.io.File;
import java.io.IOException;
import de.idrinth.stellaris.modtools.access.Queue;
import de.idrinth.stellaris.modtools.step.abstracts.TaskList;
import javax.persistence.EntityManager;
import org.apache.commons.io.FileUtils;

public class ConfigParser extends TaskList {
    private final File configuration;
    public ConfigParser(File configuration, Queue queue) {
        super(queue);
        this.configuration = configuration;
    }

    protected File getWithPrefix(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            return file;
        }
        if(!file.isAbsolute()) {
            file = new File(DirectoryLookup.getModDir().getParent() + "/" + path);
            if (file.exists()) {
                return file;
            }
        }
        throw new IOException("No valid path to mod found: " + file.getAbsolutePath());
    }

    private Runnable handlePath(String path) throws IOException, RegistryException {
        File file = getWithPrefix(path);
        if (path.endsWith(".zip")) {
            return new ZipContentParser(configuration.getName(),file,queue);
        } 
        return new FileSystemParser(configuration.getName(),file,queue);
    }
    private void persist(Modification mod) {
        mod.setConfigPath(configuration.getName());
        EntityManager manager = MainApp.entityManager.createEntityManager();
        if(!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        manager.persist(mod);
        manager.getTransaction().commit();
    }
    private void handleLine(Modification mod, String key, String value) throws RegistryException, IOException {
        switch (key) {
            case "archive":
                tasks.add(handlePath(value));
                break;
            case "remote_file_id":
                mod.setId(Integer.parseInt(value,10));
                tasks.add(new RemoteModParser(mod.getId(), queue));
                break;
            case "name":
                mod.setName(value);
                break;
            case "path":
                tasks.add(handlePath(value));
                break;
            case "supported_version":
                mod.setVersion(value);
                break;
            case "picture":
                // maybe interesting later?
                break;
            default:
            //TODO: tags, dependencies?
        }
    }
    @Override
    protected void fill() throws IOException {
        try {
            if (configuration.exists()) {
                Modification mod = new Modification();
                for (String line : FileUtils.readFileToString(configuration, "utf-8").split("\\r?\\n\\r?")) {
                    if (null != line && line.matches("\\s*[a-z_]+\\s*=\\s*\".*?\"")) {
                        String[] parts = line.trim().split("=", 2);
                        handleLine(mod, parts[0].trim(),parts[1].trim().replaceAll("^\"|\"$", ""));
                    }
                }
                persist(mod);
            }
        } catch (RegistryException | NumberFormatException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
}
