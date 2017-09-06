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
import de.idrinth.stellaris.modtools.process1datacollection.Process1Initializer;
import de.idrinth.stellaris.modtools.process2prepatchcleaning.Process2Initializer;
import de.idrinth.stellaris.modtools.process3filepatch.Process3Initializer;
import de.idrinth.stellaris.modtools.process4applypatch.Process4Initializer;
import de.idrinth.stellaris.modtools.process5modcreation.Process5Initializer;
import de.idrinth.stellaris.modtools.persistence.PersistenceProvider;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import javax.persistence.EntityManager;

public class FillerThread implements Runnable, Callable {

    private final ArrayList<ClickableTableView> list;
    private final LinkedList<ProcessHandlingQueue> tasks = new LinkedList<>();
    private final PersistenceProvider persistence = new PersistenceProvider();

    public FillerThread(ArrayList<ClickableTableView> list, ProgressElementGroup progress) {
        this.list = list;
        tasks.add(new Queue(new Process1Initializer(), this, progress, "Collecting data", persistence));
        tasks.add(new Queue(new Process2Initializer(persistence), this, progress, "Removing manually patched", persistence));
        tasks.add(new Queue(new Process3Initializer(persistence),this, progress, "Creating patches", persistence));
        tasks.add(new Queue(new Process4Initializer(persistence), this, progress, "Merging patches", persistence));
        tasks.add(new Queue(new Process5Initializer(), this, progress, "Building Mod", persistence));
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
        EntityManager manager = persistence.get();
        list.forEach((ctv) -> {
            ctv.setManager(manager);
            ctv.addItems();
        });
        return null;
    }

}
