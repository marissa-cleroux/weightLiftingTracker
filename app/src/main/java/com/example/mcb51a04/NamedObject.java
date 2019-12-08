package com.example.mcb51a04;

public class NamedObject {

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
