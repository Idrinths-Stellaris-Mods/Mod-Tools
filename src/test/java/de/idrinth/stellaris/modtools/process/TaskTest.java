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

import de.idrinth.stellaris.modtools.abstractTestCases.TestAnyTask;
import java.io.IOException;

public class TaskTest extends TestAnyTask {

    @Override
    protected ProcessTask get(ProcessHandlingQueue queue) {
        return new TaskImpl(queue);
    }

    public class TaskImpl extends Task {

        public TaskImpl(ProcessHandlingQueue queue) {
            super(queue);
        }

        @Override
        public void fill() throws IOException {
            //the point is not to do anything here - that is up to testing children of Task
        }

        @Override
        public String getIdentifier() {
            return "abc";
        }
    }
}
