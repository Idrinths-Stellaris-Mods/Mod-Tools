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
package de.idrinth.stellaris.modtools.process3filepatch;

import de.idrinth.stellaris.modtools.filesystem.DirectoryNotFoundException;
import de.idrinth.stellaris.modtools.filesystem.SteamLocation;
import de.idrinth.stellaris.modtools.persistence.entity.Patch;
import de.idrinth.stellaris.modtools.process.AbstractQueueInitializer;
import de.idrinth.stellaris.modtools.process.DataInitializer;
import de.idrinth.stellaris.modtools.persistence.PersistenceProvider;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Process3Initializer extends AbstractQueueInitializer implements DataInitializer {
    private final PersistenceProvider persistence;

    public Process3Initializer(PersistenceProvider persistence) {
        this.persistence = persistence;
    }

    @Override
    protected void init() {
        try {
            SteamLocation sloc = new SteamLocation();
            persistence.get().createNamedQuery("patch.any", Patch.class).getResultList().forEach((o) -> {
                tasks.add(new OriginalFileFiller(o.getAid(),sloc));
            });
        } catch (DirectoryNotFoundException ex) {
            Logger.getLogger(Process3Initializer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
