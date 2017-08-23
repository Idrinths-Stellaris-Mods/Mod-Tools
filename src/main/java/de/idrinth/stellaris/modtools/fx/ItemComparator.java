package de.idrinth.stellaris.modtools.fx;

import java.util.Comparator;

public class ItemComparator implements Comparator<Item> {

    @Override
    public int compare(Item o1, Item o2) {
        return o1.getKey().compareTo(o2.getKey());
    }
    
}
