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

import de.idrinth.stellaris.modtools.gui.ProgressElementGroup;
import de.idrinth.stellaris.modtools.persistence.PersistenceProvider;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class Queue implements ProcessHandlingQueue {

    private final ExecutorService executor;
    private final List<Future<?>> futures = new LinkedList<>();
    private final List<String> known = new LinkedList<>();
    private final Callable callable;
    private final ProgressElementGroup progress;
    private final PersistenceProvider persistence;
    private final DataInitializer initialDataProvider;

    public Queue(DataInitializer initialDataProvider, Callable callable, ProgressElementGroup progress, String label, PersistenceProvider persistence) {
        this.callable = callable;
        this.progress = progress;
        this.progress.addToStepLabels(label);
        this.executor = initialDataProvider.getQueueSize()>1?Executors.newFixedThreadPool(initialDataProvider.getQueueSize()):Executors.newSingleThreadExecutor();
        this.persistence = persistence;
        this.initialDataProvider = initialDataProvider;
    }

    @Override
    public synchronized void add(ProcessTask task) {
        Task lTask = new Task(this, task);
        if (!known.contains(lTask.getFullIdentifier())) {
            futures.add(executor.submit(lTask));
            known.add(lTask.getFullIdentifier());
            System.out.println("Added " + lTask.getFullIdentifier() + " to Queue");
        } else {
            System.out.println("Ignoring " + lTask.getFullIdentifier() + ", it's in already.");
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
            counter = 0;
            try {
                for (Future future : futures) {
                    if (future.isDone()) {
                        counter++;
                    }
                }
            } catch (ConcurrentModificationException e) {
                // fine, not really important
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Queue.class.getName()).log(Level.SEVERE, null, ex);
            }
            progress.update(counter, futures.size());
        } while (!isDone());
        progress.update(futures.size(), futures.size());
    }

    @Override
    public final void run() {
        while(initialDataProvider.hasNext()) {
            add(initialDataProvider.poll());
        }
        check();
        try {
            callable.call();
        } catch (Exception ex) {
            Logger.getLogger(Queue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public EntityManager getEntityManager() {
        return persistence.get();
    }
}
