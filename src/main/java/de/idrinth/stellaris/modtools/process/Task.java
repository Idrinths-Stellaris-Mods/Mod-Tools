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

import de.idrinth.stellaris.modtools.entity.TaskCompletion;
import de.idrinth.stellaris.modtools.service.PersistenceProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManager;

abstract public class Task implements Runnable {

    protected final ProcessHandlingQueue queue;
    private EntityManager entityManager;
    protected final ArrayList<Task> tasks = new ArrayList<>();

    public Task(ProcessHandlingQueue queue) {
        this.queue = queue;
    }

    abstract protected void fill() throws IOException;

    abstract protected String getIdentifier();

    @Override
    public void run() {
        try {
            TaskCompletion tk = new TaskCompletion(this.getClass().getSimpleName(), getIdentifier());
            EntityManager manager = getEntityManager();
            System.out.println("   Running " + this.getClass().getName());
            if (!manager.getTransaction().isActive()) {
                manager.getTransaction().begin();
            }
            manager.persist(tk);
            manager.getTransaction().commit();
            try {
                fill();
            } catch (Throwable ex) {
                System.out.println(ex.getLocalizedMessage());
            }
            tasks.forEach((task) -> {
                queue.add(task);
            });
            tk.setEndTime(new Date());
            if (!manager.getTransaction().isActive()) {
                manager.getTransaction().begin();
            }
            manager.persist(tk);
            manager.getTransaction().commit();
            System.out.println("   Finished " + this.getClass().getName());
        } catch (Exception e) {
            System.out.println("   Errored " + this.getClass().getName() + ": " + e.getCause().getLocalizedMessage());
        } catch (Throwable t) {
            System.err.println("failed  " + getFullIdentifier() + " with a " + t.getClass().getName());
            System.exit(1);
        }
    }

    public String getFullIdentifier() {
        return this.getClass().getName() + "@" + getIdentifier();
    }

    protected EntityManager getEntityManager() {
        if (null == entityManager) {
            entityManager = PersistenceProvider.get();
        }
        return entityManager;
    }
}
