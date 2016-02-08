package com.antoniocappiello.cloudapp.view.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.antoniocappiello.cloudapp.App;
import com.antoniocappiello.cloudapp.BuildConfig;
import com.antoniocappiello.cloudapp.R;
import com.antoniocappiello.cloudapp.presenter.backend.BackendAdapter;
import com.antoniocappiello.cloudapp.presenter.command.Command;
import com.antoniocappiello.cloudapp.presenter.command.OnAuthFailed;
import com.antoniocappiello.cloudapp.presenter.command.OnAuthSucceeded;
import com.antoniocappiello.cloudapp.Constants;
import com.antoniocappiello.cloudapp.view.DialogFactory;
import com.orhanobut.logger.Logger;
import com.pixplicity.easyprefs.library.Prefs;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.edit_text_email)
    EditText mEditTextEmailInput;

    @Bind(R.id.edit_text_password)
    EditText mEditTextPasswordInput;

    @Inject
    BackendAdapter mBackendAdapter;

    private Command mOnAuthSucceeded;
    private ProgressDialog mAuthProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ((App)getApplication()).appComponent().inject(this);
        mAuthProgressDialog = DialogFactory.getSignInProgressDialog(this);
        mOnAuthSucceeded = new OnAuthSucceeded(this);
        initPasswordInputListener();
    }

    private void initPasswordInputListener() {
        mEditTextPasswordInput.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                signInWithEmailAndPassword();
            }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBackendAdapter.addAuthStateListener(mAuthProgressDialog, mOnAuthSucceeded);
        showEmailIfAlreadyEntered();
    }

    private void showEmailIfAlreadyEntered() {
        String signupEmail = Prefs.getString(Constants.KEY_SIGNUP_EMAIL, null);
        if (signupEmail != null) {
            mEditTextEmailInput.setText(signupEmail);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mBackendAdapter.removeAuthStateListener();
    }

    @OnClick(R.id.button_sign_in_with_email_and_password)
    public void signIn() {
        signInWithEmailAndPassword();
    }

    @OnClick(R.id.text_view_sign_up)
    public void signUp() {
        startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
    }

    public void signInWithEmailAndPassword() {
        String email, password;
        if(BuildConfig.FLAVOR.equalsIgnoreCase("dev")) {
            email = BuildConfig.FIREBASE_TEST_EMAIL;
            password = BuildConfig.FIREBASE_TEST_PW;
            Logger.e(email + "\n" + password);
        }
        else {
            email = mEditTextEmailInput.getText().toString();
            password = mEditTextPasswordInput.getText().toString();
        }
        validateEmailAndPassword(email, password);
        mBackendAdapter.signIn(email, password, mAuthProgressDialog, mOnAuthSucceeded, new OnAuthFailed(this));

    }

    private void validateEmailAndPassword(String email, String password) {
        if (email.equals("")) {
            mEditTextEmailInput.setError(getString(R.string.error_cannot_be_empty));
            return;
        }
        if (password.equals("")) {
            mEditTextPasswordInput.setError(getString(R.string.error_cannot_be_empty));
            return;
        }
    }
}