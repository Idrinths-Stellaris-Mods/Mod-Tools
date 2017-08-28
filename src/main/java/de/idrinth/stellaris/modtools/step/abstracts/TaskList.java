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
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

abstract public class TaskList implements Runnable{
    protected final Queue queue;
    protected final ArrayList<Runnable> tasks = new ArrayList();

    public TaskList(Queue queue) {
        this.queue = queue;
    }

    abstract protected void fill() throws IOException;

    @Override
    public void run() {
        System.out.println("   Running "+this.getClass().getName());
        try {
            fill();
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        tasks.forEach((task) -> {
            queue.add(task);
        });
        System.out.println("   Finished "+this.getClass().getName());
    }
    protected EntityManager getEntityManager() {
        try {
            return MainApp.entityManager.createEntityManager();
        } catch(javax.persistence.PersistenceException exe) {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException ex) {
                Logger.getLogger(TaskList.class.getName()).log(Level.SEVERE, null, ex);
            }
            return getEntityManager();
        }
    }
}
