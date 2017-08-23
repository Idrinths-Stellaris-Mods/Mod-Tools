package de.idrinth.stellaris.modtools.fx;

import de.idrinth.stellaris.modtools.model.Collision;
import de.idrinth.stellaris.modtools.model.Mod;
import java.util.ArrayList;
import java.util.Collection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ObservableItemList {

    public static ObservableList<String> getCollection(Collection<Collision> map) {
        ArrayList<Collision> items = new ArrayList<>();
        map.forEach((item) -> {
            if(item.get().isPatched()) {
                items.add(item);
            }
        });
        items.sort(new ItemComparator());
        ArrayList<String> items2 = new ArrayList<>();
        items.forEach((item) -> {
            items2.add(item.toString());
        });
        items.sort(new ItemComparator());
        return FXCollections.observableArrayList (items2);
    }

    public static ObservableList<String> getMod(Collection<Mod> map) {
        ArrayList<Mod> items = new ArrayList<>();
        map.forEach((item) -> {
            items.add(item);
        });
        items.sort(new ItemComparator());
        ArrayList<String> items2 = new ArrayList<>();
        items.forEach((item) -> {
            items2.add(item.toString());
        });
        items.sort(new ItemComparator());
        return FXCollections.observableArrayList (items2);
    }
}