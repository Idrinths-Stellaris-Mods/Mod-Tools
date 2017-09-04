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
package de.idrinth.stellaris.modtools.process2prepatchcleaning;

import de.idrinth.stellaris.modtools.entity.Original;
import de.idrinth.stellaris.modtools.gui.ProgressElementGroup;
import de.idrinth.stellaris.modtools.process.AbstractQueue;
import de.idrinth.stellaris.modtools.process.ProcessHandlingQueue;
import java.util.concurrent.Callable;

public class Queue extends AbstractQueue implements ProcessHandlingQueue {

    public Queue(Callable callable, ProgressElementGroup progress) {
        super(callable, progress, "Removing manually patched");
    }

    @Override
    protected void addList() {
        getEntityManager().createNamedQuery("originals", Original.class).getResultList().forEach((o) -> {
            add(new RemoveOverwrittenFilePatch(o.getAid()));
        });
    }

}
