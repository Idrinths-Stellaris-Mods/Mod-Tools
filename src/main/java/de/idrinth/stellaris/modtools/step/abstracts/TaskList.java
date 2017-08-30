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
import de.idrinth.stellaris.modtools.entity.TaskCompletion;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManager;

abstract public class TaskList implements Runnable{
    protected final Queue queue;
    private EntityManager entityManager;
    protected final ArrayList<TaskList> tasks = new ArrayList();

    public TaskList(Queue queue) {
        this.queue = queue;
    }

    abstract protected void fill() throws IOException;
    abstract protected String getIdentifier();
    @Override
    public void run() {
        try {
            TaskCompletion tk = new TaskCompletion(this.getClass().getSimpleName(),getIdentifier());
            EntityManager manager = getEntityManager();
            System.out.println("   Running "+this.getClass().getName());
            if(!manager.getTransaction().isActive()) {
                manager.getTransaction().begin();
            }
            manager.persist(tk);
            manager.getTransaction().commit();
            try {
                fill();
            } catch (Exception ex) {
                System.out.println(ex.getLocalizedMessage());
            }
            tasks.forEach((task) -> {
                queue.add(task);
            });
            tk.setEndTime(new Date());
            if(!manager.getTransaction().isActive()) {
                manager.getTransaction().begin();
            }
            manager.persist(tk);
            manager.getTransaction().commit();
            System.out.println("   Finished "+this.getClass().getName());
        } catch (Exception e) {
            System.out.println("   Errored "+this.getClass().getName()+": "+e.getCause().getLocalizedMessage());
        }
    }
    public String getFullIdentifier() {
        return this.getClass().getName()+"@"+getIdentifier();
    }
    protected EntityManager getEntityManager() {
        if(null == entityManager) {
            entityManager = MainApp.getEntityManager();
        }
        return entityManager;
    }
}
