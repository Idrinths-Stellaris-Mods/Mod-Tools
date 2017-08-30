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
import de.idrinth.stellaris.modtools.access.DirectoryLookup;
import de.idrinth.stellaris.modtools.entity.Original;
import de.idrinth.stellaris.modtools.service.FileExtensions;
import de.idrinth.stellaris.modtools.step.abstracts.TaskList;
import java.io.File;
import java.io.IOException;
import javax.persistence.EntityManager;
import org.apache.commons.io.FileUtils;

public class OriginalFileFiller extends TaskList {
    private final String path;    
    protected final String[] extsPatch = ".txt,.yml".split(",");

    public OriginalFileFiller(String path) {
        super(null);
        this.path = path;
    }
    protected String getContent() {
        if(!FileExtensions.isPatchable(path)) {
            return "-unpatchable-";//nothing to do
        }
        try {
            File file = new File(
                    DirectoryLookup.getSteamDir().getAbsolutePath()
                    + "SteamApps/common/Stellaris/"
                    + path
                );
            return file.exists()&&file.canRead()?FileUtils.readFileToString(file, "utf-8"):"";
        } catch (IOException | RegistryException exception) {
            System.out.println(exception.getLocalizedMessage());
            return "-not readable-";
        }
    }
    @Override
    public void fill() {
        EntityManager manager = getEntityManager();
        if(!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        Original file = (Original) manager.find(Original.class, path);
        if(null == file.getContent() || "".equals(file.getContent())) {
            file.setContent(getContent());
        }
        manager.getTransaction().commit();
    }

    @Override
    protected String getIdentifier() {
        return path;
    }
    
}
