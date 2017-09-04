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
package de.idrinth.stellaris.modtools.process;

import de.idrinth.stellaris.modtools.gui.ClickableTableView;
import de.idrinth.stellaris.modtools.gui.ProgressElementGroup;
import de.idrinth.stellaris.modtools.service.PersistenceProvider;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import javax.persistence.EntityManager;

public class FillerThread implements Runnable, Callable {

    private final ArrayList<ClickableTableView> list;
    private final LinkedList<ProcessHandlingQueue> tasks = new LinkedList<>();

    public FillerThread(ArrayList<ClickableTableView> list, ProgressElementGroup progress) {
        this.list = list;
        tasks.add(new de.idrinth.stellaris.modtools.process1datacollection.Queue(this, progress));
        tasks.add(new de.idrinth.stellaris.modtools.process2prepatchcleaning.Queue(this, progress));
        tasks.add(new de.idrinth.stellaris.modtools.process3filepatch.Queue(this, progress));
        tasks.add(new de.idrinth.stellaris.modtools.process4applypatch.Queue(this, progress));
        tasks.add(new de.idrinth.stellaris.modtools.process5modcreation.Queue(this, progress));
    }

    @Override
    public void run() {
        try {
            call();
        } catch (Exception exception) {
            System.out.println(exception.getLocalizedMessage());
        }
    }

    @Override
    public synchronized Object call() throws Exception {
        if (!tasks.isEmpty()) {
            new Thread(tasks.poll()).start();
            return null;
        }
        EntityManager manager = PersistenceProvider.get();
        list.forEach((ctv) -> {
            ctv.setManager(manager);
            ctv.addItems();
        });
        return null;
    }

}
