package com.antoniocappiello.cloudapp.ui.screen.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.antoniocappiello.cloudapp.App;
import com.antoniocappiello.cloudapp.BuildConfig;
import com.antoniocappiello.cloudapp.R;
import com.antoniocappiello.cloudapp.model.Account;
import com.antoniocappiello.cloudapp.service.action.OpenEmailAppAction;
import com.antoniocappiello.cloudapp.service.action.ShowToastSignInFailedAction;
import com.antoniocappiello.cloudapp.service.backend.BackendAdapter;
import com.antoniocappiello.cloudapp.service.utils.PasswordGenerator;
import com.antoniocappiello.cloudapp.ui.customwidget.ProgressDialogFactory;
import com.antoniocappiello.cloudapp.ui.screen.BaseActivity;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateAccountActivity extends BaseActivity {

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
        if(BuildConfig.FLAVOR.equalsIgnoreCase("dev")) {
            userName = userEmail = BuildConfig.FIREBASE_TEST_EMAIL;
            Logger.e(userEmail + "\n" + userName);
        }
        else {
            userEmail = mEditTextEmailCreate.getText().toString();
            userName = mEditTextUsernameCreate.getText().toString();
        }
        String password = PasswordGenerator.generateRandomPassword();

        if (!isEmailValid(userEmail) || !isUserNameValid(userName))
            return;

        Account account = new Account(userName, userEmail, password);

        mBackendAdapter.createUser(account, ProgressDialogFactory.getSignUpProgressDialog(this), new ShowToastSignInFailedAction(this), new OpenEmailAppAction(this));

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
