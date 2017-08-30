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
package de.idrinth.stellaris.modtools.access;

import de.idrinth.stellaris.modtools.FillerThread;
import de.idrinth.stellaris.modtools.MainApp;
import de.idrinth.stellaris.modtools.entity.Original;
import de.idrinth.stellaris.modtools.step.PatchFile;
import de.idrinth.stellaris.modtools.step.abstracts.TaskList;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class Queue implements Runnable {

    private final ExecutorService executor = Executors.newFixedThreadPool(15);//75% of maximum, just to be on the save side
    private final List<Future<?>> futures = new ArrayList<>();
    private final List<String> known = new ArrayList<>();
    private final FillerThread c;

    public Queue(FillerThread c) {
        this.c = c;
    }

    public synchronized void add(TaskList task) {
        if(!known.contains(task.getFullIdentifier())) {
            futures.add(executor.submit(task));
            known.add(task.getFullIdentifier());
        } else {
            System.out.println("Ignoring "+task.getFullIdentifier()+", it's in already.");
        }
    }

    private synchronized boolean isDone() {
        boolean done = true;
        done = futures.stream().map((future) -> future.isDone()).reduce(done, (accumulator, _item) -> accumulator & _item); // check if future is done
        return done;
    }
    private void check() {
        int counter;
        do {
            counter =0;
            for(Future future:futures) {
                if(!future.isDone()) {
                    counter++;
                }
            }
            try {
                Thread.sleep(2500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Queue.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(counter+" of "+futures.size()+" todo; is considered "+(isDone()?"":"not ")+"done");
        } while(!isDone());
    }
    @Override
    public void run() {
        check();
        EntityManager manager = MainApp.getEntityManager();
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
            Logger.getLogger(Queue.class.getName()).log(Level.SEVERE, null, ex);
        }
        ExecutorService local = Executors.newFixedThreadPool(15);
        manager.createNamedQuery("originals",Original.class).getResultList().forEach((o) -> {
            System.out.println("adding "+o.getRelativePath());
            local.submit(new PatchFile(o.getRelativePath()));
        });
        local.shutdown();
        try {
            local.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
            Logger.getLogger(Queue.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            c.call();
        } catch (Exception ex) {
            Logger.getLogger(Queue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
