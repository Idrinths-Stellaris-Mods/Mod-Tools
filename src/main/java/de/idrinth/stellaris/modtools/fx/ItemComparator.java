package de.idrinth.stellaris.modtools.fx;

import java.util.Comparator;

public class ItemComparator implements Comparator<Item> {

    @Override
    public int compare(Item o1, Item o2) {
        if(o1 == null || o1.getKey() == null) {
            return 1;
        }
        if(o2 == null || o2.getKey() == null) {
            return -1;
        }
        return o1.getKey().compareTo(o2.getKey());
    }
    
}
