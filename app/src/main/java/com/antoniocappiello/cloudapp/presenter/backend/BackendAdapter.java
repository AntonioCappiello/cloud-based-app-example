package com.antoniocappiello.cloudapp.presenter.backend;

import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;

import com.antoniocappiello.cloudapp.model.Account;
import com.antoniocappiello.cloudapp.presenter.command.Command;
import com.antoniocappiello.cloudapp.presenter.command.OnAuthFailed;
import com.antoniocappiello.cloudapp.presenter.command.OnSignUpSucceeded;
import com.antoniocappiello.cloudapp.view.list.ItemViewHolder;

import java.util.List;

import rx.Observable;

public interface BackendAdapter<T> {

    void addItemToUserList(String userEmail, T item);

    Observable<List<T>> readItems();

    RecyclerView.Adapter<ItemViewHolder> getRecyclerViewAdapterForUserItemList(String userEmail);

    void cleanup();

    void addAuthStateListener(ProgressDialog signInProgressDialog, Command onAuthSucceeded);

    void removeAuthStateListener();

    void signIn(String email, String password, ProgressDialog signInProgressDialog, Command onAuthSucceeded, Command onAuthFailed);

    void signOut();

    void createUser(Account account, ProgressDialog signUpProgressDialog, OnAuthFailed onAuthFailed, OnSignUpSucceeded onSignUpSucceeded);
}
