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

class Task implements Runnable {

    protected final ProcessHandlingQueue queue;
    private final ProcessTask task;

    public Task(ProcessHandlingQueue queue, ProcessTask task) {
        this.queue = queue;
        this.task = task;
    }

    @Override
    public void run() {
        try {
            System.out.println("   Running " + getFullIdentifier());
            try {
                task.handle( queue.getEntityManager()).forEach((newTask) -> {
                    queue.add(newTask);
                });
            } catch (Throwable ex) {
                System.out.println(ex.getLocalizedMessage());
            }
            System.out.println("   Finished " + getFullIdentifier());
        } catch (Exception e) {
            System.out.println("   Errored " + getFullIdentifier() + ": " + e.getCause().getLocalizedMessage());
        } catch (Throwable t) {
            System.err.println("failed  " + getFullIdentifier() + " with a " + t.getClass().getName());
            System.exit(1);
        }
    }

    public String getFullIdentifier() {
        return this.task.getClass().getName() + "@" + task.getIdentifier();
    }
}
