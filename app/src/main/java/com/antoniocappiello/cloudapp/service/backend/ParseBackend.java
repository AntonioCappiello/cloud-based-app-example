package com.antoniocappiello.cloudapp.service.backend;

import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;

import com.antoniocappiello.cloudapp.model.Account;
import com.antoniocappiello.cloudapp.model.Item;
import com.antoniocappiello.cloudapp.service.action.Action;
import com.antoniocappiello.cloudapp.ui.screen.itemlist.ItemViewHolder;

import java.util.List;

import rx.Observable;

public class ParseBackend implements BackendAdapter<Item> {
    @Override
    public void addItemToUserList(Item item) {

    }

    @Override
    public Observable<List<Item>> readItems() {
        return null;
    }

    @Override
    public RecyclerView.Adapter<ItemViewHolder> getRecyclerViewAdapterForUserItemList() {
        return null;
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void addAuthStateListener(Action onAuthenticated, Action onUnAuthenticated) {

    }

    @Override
    public void removeAuthStateListener() {

    }

    @Override
    public void signIn(String email, String password, ProgressDialog signInProgressDialog, Action onAuthSucceeded, Action onAuthFailed) {

    }

    @Override
    public void logOut() {

    }

    @Override
    public void createUser(Account account, ProgressDialog signUpProgressDialog, Action onSignInFailed, Action onSignUpSucceeded) {

    }

    @Override
    public String getCurrentUserEmail() {
        return null;
    }

    @Override
    public void updateItemInUserList(String itemId, Item item) {

    }
}
