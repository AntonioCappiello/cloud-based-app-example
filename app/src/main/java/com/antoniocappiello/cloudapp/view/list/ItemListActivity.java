package com.antoniocappiello.cloudapp.view.list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.antoniocappiello.cloudapp.App;
import com.antoniocappiello.cloudapp.R;
import com.antoniocappiello.cloudapp.Utils;
import com.antoniocappiello.cloudapp.model.Item;
import com.antoniocappiello.cloudapp.presenter.backend.BackendAdapter;
import com.antoniocappiello.cloudapp.view.BaseActivity;
import com.antoniocappiello.cloudapp.view.widgets.DialogFactory;
import com.orhanobut.logger.Logger;

import java.util.Date;
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

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mBackendAdapter.getRecyclerViewAdapterForUserItemList(Utils.getCurrentUserEmail()));
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
        DialogFactory.getAddItemDialog(this).show();
//        Item item = new Item("DUMMY NAME", new Date(System.currentTimeMillis()).toString()); // dummy data
//        mBackendAdapter.addItemToUserList(Utils.getCurrentUserEmail(), item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBackendAdapter.cleanup();
    }
}
