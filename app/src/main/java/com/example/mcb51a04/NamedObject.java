package com.example.mcb51a04;

import java.io.Serializable;

public class NamedObject implements Serializable {

    private String name;

    public NamedObject(String name) {
        this.name = name;
    }

    public NamedObject() {
        this.name = "unknown";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
