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

import de.idrinth.stellaris.modtools.abstractTestCases.TestAnyQueue;
import de.idrinth.stellaris.modtools.gui.ProgressElementGroup;
import java.util.concurrent.Callable;

public class AbstractQueueTest extends TestAnyQueue {

    @Override
    protected ProcessHandlingQueue get(ProgressElementGroup progress, Callable callable) {
        return new AbstractQueueImpl(callable, progress);
    }
    private class AbstractQueueImpl extends AbstractQueue {
        public AbstractQueueImpl(Callable callable, ProgressElementGroup progress) {
            super(callable, progress, "");
        }

        @Override
        public void addList() {
        }
    }
}
