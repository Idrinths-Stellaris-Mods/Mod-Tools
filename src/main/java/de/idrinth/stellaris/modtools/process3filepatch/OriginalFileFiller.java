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
package de.idrinth.stellaris.modtools.process3filepatch;

import de.idrinth.stellaris.modtools.persistence.entity.Original;
import de.idrinth.stellaris.modtools.process.ProcessTask;
import de.idrinth.stellaris.modtools.filesystem.FileExtensions;
import de.idrinth.stellaris.modtools.filesystem.FileSystemLocation;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.io.FileUtils;

class OriginalFileFiller implements ProcessTask {

    private final long aid;
    private final FileSystemLocation steamDir;

    public OriginalFileFiller(long aid, FileSystemLocation steamDir) {
        this.aid = aid;
        this.steamDir = steamDir;
    }

    protected String getContent(String path) {
        if (!FileExtensions.isPatchable(path)) {
            return "-unpatchable-";//nothing to do
        }
        try {
            File file = new File(
                    steamDir.get().getAbsolutePath()
                    + "steamapps/common/Stellaris/"//at least ubutu's steam uses lower case
                    + path
            );
            return file.exists() && file.canRead() ? FileUtils.readFileToString(file, "utf-8") : "";
        } catch (IOException exception) {
            System.out.println(exception.getLocalizedMessage());
            return "-not readable-";
        }
    }

    @Override
    public List<ProcessTask> handle(EntityManager manager) {
        if (!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        Original file = (Original) manager.find(Original.class, aid);
        if (null == file.getContent() || "".equals(file.getContent())) {
            file.setContent(getContent(file.getRelativePath()));
        }
        manager.getTransaction().commit();
        ArrayList<ProcessTask> list = new ArrayList<>();
        list.add(new GenerateFilePatch(aid));
        return list;
    }

    @Override
    public String getIdentifier() {
        return String.valueOf(aid);
    }

}
