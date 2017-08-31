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
package de.idrinth.stellaris.modtools.service;

import com.sksamuel.diffpatch.DiffMatchPatch;
import java.util.LinkedList;

/**
 *
 * @author bbuettner
 */
public class MultiDiffPatch {
    private boolean patchable;
    private final String original;
    private int patched = 0;
    private String current;
    private final DiffMatchPatch patcher = new DiffMatchPatch();

    public MultiDiffPatch(boolean patchable, String original) {
        this.patchable = patchable;
        this.original = original;
        this.current = patchable?original:"file type can't be patched, but shouldn't break anything.";
    }
    
    public void addText(String patch) {
        patched++;
        if(!patchable) {
            System.out.println("not patchable, nothing to do");
            return;
        }
        Object[] dat = patcher.patch_apply((LinkedList) patcher.patch_fromText(patch), current);
        for(boolean s:(boolean[]) dat[1]) {
            patchable = patchable && s;
        }
        current = patchable?(String) dat[0]:"Failed patching automatically";
        System.out.println("applied new patch");
    }
    public String getResult() {
        return patched<2?"No patching needed":current;
    }
}
