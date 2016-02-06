package com.antoniocappiello.cloudapp.model;

public class Item {

    private String itemName;
    private String timestamp;

    public Item() {
    }

    public Item(String itemName, String timestamp) {
        this.itemName = itemName;
        this.timestamp = timestamp;
    }

    public String getItemName() {
        return itemName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemName='" + itemName + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
