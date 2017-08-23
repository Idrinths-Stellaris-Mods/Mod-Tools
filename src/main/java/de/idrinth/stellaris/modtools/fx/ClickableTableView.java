package de.idrinth.stellaris.modtools.fx;

import java.util.Collection;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

abstract public class ClickableTableView<E,T> extends TableView<E> {
    protected E current;
    protected boolean modified;
    public ClickableTableView(String[] columns) {
        setRowFactory( tv -> {
            TableRow<E> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    current = row.getItem();
                    modified = true;
                }
            });
            return row ;
        });
        for(String column:columns) {
            addColumn(column,300/columns.length);
        }
    }
    private void addColumn(String name,int width) {
        TableColumn<E,String> column = new TableColumn<>(name);
        column.setCellValueFactory(
            new PropertyValueFactory<>(name)
        );
        column.setMinWidth(width);
        getColumns().add(column);
    }
    public abstract void addItems(Collection<T> items);
    public E getCurrent() {
        if(!modified) {
            return null;
        }
        modified = false;
        return current;
    }
}
