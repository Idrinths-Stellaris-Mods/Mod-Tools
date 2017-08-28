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
package de.idrinth.stellaris.modtools;

import de.idrinth.stellaris.modtools.access.DirectoryLookup;
import de.idrinth.stellaris.modtools.access.Queue;
import de.idrinth.stellaris.modtools.filter.FileExt;
import de.idrinth.stellaris.modtools.fx.ClickableTableView;
import de.idrinth.stellaris.modtools.step.ConfigParser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import javax.persistence.EntityManager;

public class FillerThread implements Runnable,Callable {
    private final ArrayList<ClickableTableView> list;

    public FillerThread(ArrayList<ClickableTableView> list) {
        this.list = list;
    }
    @Override
    public void run() {
        try {
            Queue queue = new Queue(this);
            for (File mod : DirectoryLookup.getModDir().listFiles(new FileExt("mod"))) {
                queue.add(new ConfigParser(mod,queue));
            }
            new Thread(queue).start();
        } catch (IOException exception) {
            System.out.println(exception.getLocalizedMessage());
        }
    }

    @Override
    public Object call() throws Exception {
        EntityManager manager = MainApp.entityManager.createEntityManager();
        list.forEach((ctv) -> {
            ctv.setManager(manager);
            ctv.addItems();
        });
        return null;
    }
    
}
