package com.antoniocappiello.cloudapp.ui.screen.itemlist;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.antoniocappiello.cloudapp.App;
import com.antoniocappiello.cloudapp.R;
import com.antoniocappiello.cloudapp.model.Item;
import com.antoniocappiello.cloudapp.service.backend.BackendAdapter;
import com.antoniocappiello.cloudapp.service.event.EditItemEvent;
import com.antoniocappiello.cloudapp.ui.screen.BaseActivity;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

public class ItemListActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Inject
    BackendAdapter mBackendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ((App)getApplication()).appComponent().inject(this);
        initRecyclerView();
        //loadData();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mBackendAdapter.getRecyclerViewAdapterForUserItemList());
    }

    private void loadData() {
        Action1<List<Item>> updateViewAction = items -> {
            for(Item item: items)
                Logger.d(item.toString());
        };
        mBackendAdapter.readItems().subscribe(updateViewAction);
    }

    @OnClick(R.id.fab)
    public void addItem(){
        new AddItemDialogFragment().show(getFragmentManager(), "AddItemDialogFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBackendAdapter.cleanup();
    }

    @Subscribe
    public void onEvent(EditItemEvent editItemEvent){
        EditItemDialogFragment dialog = EditItemDialogFragment.newInstance(
                editItemEvent.getItemId(),
                editItemEvent.getItem());
        dialog.show(getFragmentManager(), "EditItemDialogFragment");
    }
}
