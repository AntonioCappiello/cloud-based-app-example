package com.antoniocappiello.cloudapp.view.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.antoniocappiello.cloudapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ItemViewHolder extends RecyclerView.ViewHolder{

    @Bind(R.id.name)
    TextView mNameTextView;

    @Bind(R.id.timestamp)
    TextView mTimestampTextView;

    public ItemViewHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public TextView getNameTextView() {
        return mNameTextView;
    }

    public TextView getTimestampTextView() {
        return mTimestampTextView;
    }
}
