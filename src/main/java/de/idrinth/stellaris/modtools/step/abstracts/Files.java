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
package de.idrinth.stellaris.modtools.step.abstracts;

import de.idrinth.stellaris.modtools.MainApp;
import de.idrinth.stellaris.modtools.access.Queue;
import de.idrinth.stellaris.modtools.entity.Modification;
import de.idrinth.stellaris.modtools.entity.Original;
import de.idrinth.stellaris.modtools.entity.Patch;
import de.idrinth.stellaris.modtools.step.OriginalFileFiller;
import javax.persistence.EntityManager;

abstract public class Files extends TaskList {
    protected final String modConfigName;
    protected final String[] exts = ".txt,.yml,.dds,.gfx,.gui".split(",");

    public Files(Queue queue, String modConfigName) {
        super(queue);
        this.modConfigName = modConfigName;
    }

    protected void addToFiles(String fPath, Modification mod, String content) {
        if (fPath.contains("/")) {
            EntityManager manager = MainApp.entityManager.createEntityManager();
            Original file = (Original) manager.find(Original.class, fPath);
            if(null == file) {
                file = new Original();
                file.setRelativePath(fPath);
                queue.add(new OriginalFileFiller(fPath));
            }
            Patch patch = new Patch();
            file.getPatches().add(patch);
            patch.setDiff(content);
            patch.setFile(file);
            patch.setMod(mod);
        }
    }

}
