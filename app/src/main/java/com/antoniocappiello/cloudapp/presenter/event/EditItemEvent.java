package com.antoniocappiello.cloudapp.presenter.event;

import com.antoniocappiello.cloudapp.model.Item;

public class EditItemEvent {
    private final Item mItem;
    private final String mItemId;

    public EditItemEvent(String itemId, Item item) {
        mItemId = itemId;
        mItem = item;
    }

    public Item getItem() {
        return mItem;
    }

    public String getItemId() {
        return mItemId;
    }
}
