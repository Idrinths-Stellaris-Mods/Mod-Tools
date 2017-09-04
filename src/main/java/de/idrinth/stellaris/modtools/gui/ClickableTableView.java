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
package de.idrinth.stellaris.modtools.gui;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.EntityManager;

abstract public class ClickableTableView<E, T> extends TableView<E> {

    protected E current;
    protected boolean modified;
    protected EntityManager manager;

    public ClickableTableView(String[] columns) {
        setRowFactory(tv -> {
            TableRow<E> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    current = row.getItem();
                    modified = true;
                }
            });
            return row;
        });
        for (String column : columns) {
            addColumn(column, 600 / columns.length);
        }
    }

    public void setManager(EntityManager manager) {
        this.manager = manager;
    }

    private void addColumn(String name, int width) {
        TableColumn<E, String> column = new TableColumn<>(name);
        column.setCellValueFactory(
                new PropertyValueFactory<>(name)
        );
        column.setMinWidth(width);
        getColumns().add(column);
    }

    public abstract void addItems();

    public E getCurrent() {
        if (!modified) {
            return null;
        }
        modified = false;
        return current;
    }
}
