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

import de.idrinth.stellaris.modtools.MainApp;
import de.idrinth.stellaris.modtools.entity.Modification;
import java.util.Collection;

public class ModTableView extends ClickableTableView<ModDataRow, ModDataRow> {

    public ModTableView() {
        super("Id,Name,Version,Collisions".split(","));
    }

    public final void addItems() {
        super.getItems().clear();
        MainApp.entityManager.createEntityManager().createNamedQuery("all_modifications", Modification.class).getResultList().forEach((mod) -> {
            super.getItems().add(new ModDataRow(mod));
        });
    }

    @Override
    public void addItems(Collection<ModDataRow> items) {
    }
}
