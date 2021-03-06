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
package de.idrinth.stellaris.modtools.process4applypatch;

import de.idrinth.stellaris.modtools.abstract_cases.TestAnyTask;
import de.idrinth.stellaris.modtools.process.ProcessTask;
import java.util.LinkedList;

public class ApplyPatchFileTest extends TestAnyTask {

    @Override
    protected ProcessTask get() {
        LinkedList<Long> ll = new LinkedList<>();
        ll.add((long) 1);
        ll.add((long) 2);
        return new ApplyPatchFile(ll, 1);
    }
}
