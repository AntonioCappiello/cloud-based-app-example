package com.antoniocappiello.cloudapp.ui.screen.itemlist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.antoniocappiello.cloudapp.R;
import com.antoniocappiello.cloudapp.model.Item;
import com.antoniocappiello.cloudapp.service.event.EditItemEvent;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @Bind(R.id.name)
    TextView mNameTextView;

    @Bind(R.id.timestamp)
    TextView mTimestampTextView;

    @Bind(R.id.description)
    TextView mDescriptionTextView;

    private Item mItem;
    private String mItemId;

    public ItemViewHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    public void updateView(String itemId, Item item) {
        mItemId = itemId;
        mItem = item;
        mNameTextView.setText(item.getName());
        mDescriptionTextView.setText(item.getDescription());
        mTimestampTextView.setText(item.getTimestamp());
    }

    @Override
    public void onClick(View v) {
        Logger.d("clicked " + mItem.getName());
        EventBus.getDefault().post(new EditItemEvent(mItemId, mItem));
    }
}
