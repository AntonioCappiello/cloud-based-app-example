package com.antoniocappiello.cloudapp.presenter.backend;

import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;

import com.antoniocappiello.cloudapp.model.Account;
import com.antoniocappiello.cloudapp.presenter.command.Command;
import com.antoniocappiello.cloudapp.presenter.command.OnSignInFailed;
import com.antoniocappiello.cloudapp.presenter.command.OnSignUpSucceeded;
import com.antoniocappiello.cloudapp.view.list.ItemViewHolder;

import java.util.List;

import rx.Observable;

public interface BackendAdapter<T> {

    void addItemToUserList(String userEmail, T item);

    Observable<List<T>> readItems();

    RecyclerView.Adapter<ItemViewHolder> getRecyclerViewAdapterForUserItemList(String userEmail);

    void cleanup();

    void addAuthStateListener(Command onAuthenticated, Command onUnAuthenticated);

    void removeAuthStateListener();

    void signIn(String email, String password, ProgressDialog signInProgressDialog, Command onAuthSucceeded, Command onAuthFailed);

    void logOut();

    void createUser(Account account, ProgressDialog signUpProgressDialog, OnSignInFailed onSignInFailed, OnSignUpSucceeded onSignUpSucceeded);
}
