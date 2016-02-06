package com.antoniocappiello.cloudapp.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.antoniocappiello.cloudapp.App;
import com.antoniocappiello.cloudapp.BuildConfig;
import com.antoniocappiello.cloudapp.R;
import com.antoniocappiello.cloudapp.Utils;
import com.antoniocappiello.cloudapp.model.Account;
import com.antoniocappiello.cloudapp.presenter.backend.BackendAdapter;
import com.antoniocappiello.cloudapp.presenter.command.OnAuthFailed;
import com.antoniocappiello.cloudapp.presenter.command.OnSignUpSucceeded;
import com.antoniocappiello.cloudapp.view.DialogFactory;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateAccountActivity extends AppCompatActivity {

    @Bind(R.id.edit_text_username_create)
    EditText mEditTextUsernameCreate;

    @Bind(R.id.edit_text_email_create)
    EditText mEditTextEmailCreate;

    @Inject
    BackendAdapter mBackendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ButterKnife.bind(this);
        ((App)getApplication()).appComponent().inject(this);
    }

    @OnClick(R.id.text_view_sign_in)
    public void signIn() {
        Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.button_create_account)
    public void createUser() {
        String userEmail, userName;
        if(BuildConfig.DEBUG) {
            userName = userEmail = BuildConfig.FIREBASE_TEST_EMAIL;
            Logger.e(userEmail + "\n" + userName);
        }
        else {
            userEmail = mEditTextEmailCreate.getText().toString();
            userName = mEditTextUsernameCreate.getText().toString();
        }
        String password = Utils.generateRandomPassword();

        if (!isEmailValid(userEmail) || !isUserNameValid(userName))
            return;

        Account account = new Account(userName, userEmail, password);

        mBackendAdapter.createUser(account, DialogFactory.getSignUpProgressDialog(this), new OnAuthFailed(this), new OnSignUpSucceeded(this));

    }

    private boolean isEmailValid(String email) {
        boolean isValid = (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isValid) {
            mEditTextEmailCreate.setError(String.format(getString(R.string.error_invalid_email_not_valid), email));
            return false;
        }
        return isValid;
    }

    private boolean isUserNameValid(String userName) {
        if (userName.equals("")) {
            mEditTextUsernameCreate.setError(getResources().getString(R.string.error_cannot_be_empty));
            return false;
        }
        return true;
    }

}
