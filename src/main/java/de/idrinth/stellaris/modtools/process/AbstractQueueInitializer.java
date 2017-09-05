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

import java.util.LinkedList;

abstract public class AbstractQueueInitializer implements DataInitializer {
    private boolean isInitialized = false;
    protected final LinkedList<ProcessTask> tasks = new LinkedList<>();

    abstract protected void init();
    
    private void initOnce() {
        if(isInitialized) {
            return;
        }
        isInitialized = true;
        init();
    }

    @Override
    public final ProcessTask poll() {
        initOnce();
        return tasks.poll();
    }

    @Override
    public final boolean hasNext() {
        initOnce();
        return !tasks.isEmpty();
    }

    @Override
    public int getQueueSize() {
        return 20;
    }
}
