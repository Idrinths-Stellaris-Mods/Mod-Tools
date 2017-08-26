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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Queue implements Runnable {

    private final ExecutorService executor = new ThreadPoolExecutor(5, 5, 100, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());//Executors.newFixedThreadPool(5);//25% of maximum, just to be on the save side
    private final List<Future<?>> futures = new ArrayList<>();
    private final FillerThread c;

    public Queue(FillerThread c) {
        this.c = c;
    }

    public synchronized void add(Runnable task) {
        futures.add(executor.submit(task));
    }

    private synchronized boolean isDone() {
        boolean done = true;
        done = futures.stream().map((future) -> future.isDone()).reduce(done, (accumulator, _item) -> accumulator & _item); // check if future is done
        return done;
    }

    @Override
    public void run() {
        int counter;
        do {
            counter =0;
            for(Future future:futures) {
                if(!future.isDone()) {
                    counter++;
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Queue.class.getName()).log(Level.SEVERE, null, ex);
            }
            c.setProgress(counter, futures.size());
        } while(counter< futures.size()&&!isDone());
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
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
