package com.antoniocappiello.cloudapp.model;

public class Item {

    private String name;
    private String timestamp;
    private String description;

    public Item() {
    }

    public Item(String name, String description, String timestamp) {
        this.name = name;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Item{" +
                "description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
