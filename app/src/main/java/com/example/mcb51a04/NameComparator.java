package com.example.mcb51a04;

import java.util.Comparator;

public class NameComparator implements Comparator<NamedObject> {

    @Override
    public int compare(NamedObject o1, NamedObject o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}
