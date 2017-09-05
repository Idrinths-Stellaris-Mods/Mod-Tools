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
import de.idrinth.stellaris.modtools.service.DirectoryLookup;
import de.idrinth.stellaris.modtools.entity.Original;
import de.idrinth.stellaris.modtools.process.ProcessTask;
import de.idrinth.stellaris.modtools.service.FileExtensions;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.io.FileUtils;

class OriginalFileFiller implements ProcessTask {

    private final String path;

    public OriginalFileFiller(String path) {
        this.path = path;
    }

    protected String getContent() {
        if (!FileExtensions.isPatchable(path)) {
            return "-unpatchable-";//nothing to do
        }
        try {
            File file = new File(
                    DirectoryLookup.getSteamDir().getAbsolutePath()
                    + "steamapps/common/Stellaris/"//at least ubutu's steam uses lower case
                    + path
            );
            return file.exists() && file.canRead() ? FileUtils.readFileToString(file, "utf-8") : "";
        } catch (IOException | RegistryException exception) {
            System.out.println(exception.getLocalizedMessage());
            return "-not readable-";
        }
    }

    @Override
    public List<ProcessTask> handle(EntityManager manager) {
        if (!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        Original file = (Original) manager.createNamedQuery("original.path", Original.class)
                .setParameter("path", path)
                .getSingleResult();
        if (null == file.getContent() || "".equals(file.getContent())) {
            file.setContent(getContent());
        }
        manager.getTransaction().commit();
        return new ArrayList<>();
    }

    @Override
    public String getIdentifier() {
        return path;
    }

}
