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
package de.idrinth.stellaris.modtools.fx;

import de.idrinth.stellaris.modtools.model.Collision;
import de.idrinth.stellaris.modtools.model.PatchedFile;
import java.util.Collection;

public class CollisionTableView extends ClickableTableView<PatchedFile, Collision> {

    public CollisionTableView() {
        super("File,Mods".split(","));
    }

    @Override
    public final void addItems(Collection<Collision> collisions) {
        super.getItems().clear();
        collisions.forEach((collision) -> {
            PatchedFile file = collision.get();
            if (file.isPatched()) {
                super.getItems().add(file);
            }
        });
    }
}
