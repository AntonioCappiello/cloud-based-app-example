package com.antoniocappiello.cloudapp.ui.screen.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.antoniocappiello.cloudapp.App;
import com.antoniocappiello.cloudapp.BuildConfig;
import com.antoniocappiello.cloudapp.R;
import com.antoniocappiello.cloudapp.service.action.Action;
import com.antoniocappiello.cloudapp.service.action.ShowItemListScreenAction;
import com.antoniocappiello.cloudapp.service.action.ShowToastSignInFailedAction;
import com.antoniocappiello.cloudapp.service.backend.BackendAdapter;
import com.antoniocappiello.cloudapp.ui.customwidget.ProgressDialogFactory;
import com.antoniocappiello.cloudapp.ui.screen.BaseActivity;
import com.antoniocappiello.socialauth.AuthService;
import com.antoniocappiello.socialauth.OAuthTokenHandler;
import com.antoniocappiello.socialauth.provider.AuthProvider;
import com.antoniocappiello.socialauth.provider.AuthProviderType;
import com.antoniocappiello.socialauth.provider.facebook.FacebookAuthProvider;
import com.antoniocappiello.socialauth.provider.google.GoogleAuthProvider;
import com.antoniocappiello.socialauth.provider.twitter.TwitterAuthProvider;
import com.google.android.gms.common.api.GoogleApiClient;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {

    @Bind(R.id.edit_text_email)
    EditText mEditTextEmailInput;

    @Bind(R.id.edit_text_password)
    EditText mEditTextPasswordInput;

    @Bind(R.id.button_sign_in_with_google)
    View mButtonSignInWithGoogle;

    @Bind(R.id.button_sign_in_with_facebook)
    View mButtonSignInWithFacebook;

    @Bind(R.id.button_sign_in_with_twitter)
    View mButtonSignInWithTwitter;

    @Inject
    BackendAdapter mBackendAdapter;

    private Action mOnSignInSucceeded;
    private Action mOnSignInFailed;
    private ProgressDialog mAuthProgressDialog;
    private AuthService mAuthService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ((App) getApplication()).appComponent().inject(this);
        initPasswordInputListener();
        mAuthProgressDialog = ProgressDialogFactory.getSignInProgressDialog(this);
        mOnSignInSucceeded = new ShowItemListScreenAction(this);
        mOnSignInFailed = new ShowToastSignInFailedAction(this);

        /**
         * Create TWITTER Authentication Provider
         */
        OAuthTokenHandler twitterOAuthTokenHandler = new OAuthTokenHandler(AuthProviderType.TWITTER, mBackendAdapter);

        AuthProvider twitterAuthProvider = new TwitterAuthProvider.Builder()
                .activity(this)
                .signInView(mButtonSignInWithTwitter)
                .oAuthTokenHandler(twitterOAuthTokenHandler)
                .build();

        /**
         * Create FACEBOOK Authentication Provider
         */
        OAuthTokenHandler facebookOAuthTokenHandler = new OAuthTokenHandler(AuthProviderType.FACEBOOK, mBackendAdapter);

        AuthProvider facebookAuthProvider = new FacebookAuthProvider.Builder()
                .activity(this)
                .signInView(mButtonSignInWithFacebook)
                .oAuthTokenHandler(facebookOAuthTokenHandler)
                .build();

        /**
         * Create GOOGLE Authentication Provider
         */
        OAuthTokenHandler googleOAuthTokenHandler = new OAuthTokenHandler(AuthProviderType.GOOGLE, mBackendAdapter);
        GoogleApiClient.ConnectionCallbacks googleConnectionCallback = getGoogleConnectionCallback();
        GoogleApiClient.OnConnectionFailedListener googleOnConnectionFailedListener = getGoogleOnConnectionFailedListener();

        AuthProvider googleAuthProvider = new GoogleAuthProvider.Builder()
                .activity(this)
                .signInView(mButtonSignInWithGoogle)
                .oAuthTokenHandler(googleOAuthTokenHandler)
                .connectionCallback(googleConnectionCallback)
                .onConnectionFailedListener(googleOnConnectionFailedListener)
                .build();

        /**
         * Create Authentication Service with Twitter, Facebook and Google Auth providers
         */
        mAuthService = new AuthService()
                .enableAuthProvider(googleAuthProvider)
                .enableAuthProvider(facebookAuthProvider)
                .enableAuthProvider(twitterAuthProvider);
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
        showEmailIfAlreadyEntered();
        mBackendAdapter.addAuthStateListener(mOnSignInSucceeded, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBackendAdapter.removeAuthStateListener();
    }

    private void showEmailIfAlreadyEntered() {
        String userEmail = mBackendAdapter.getCurrentUserEmail();
        if (userEmail != null && !userEmail.isEmpty()) {
            mEditTextEmailInput.setText(userEmail);
        }
    }

    @OnClick(R.id.button_sign_in_with_email_and_password)
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
        if(isEmailValid(email) && isPasswordValid(password)) {
            mBackendAdapter.signIn(email, password, mAuthProgressDialog, mOnSignInSucceeded, mOnSignInFailed);
        }
    }

    @OnClick(R.id.text_view_sign_up)
    public void signUp() {
        startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
    }

    private boolean isEmailValid(String email) {
        if (email.equals("")) {
            mEditTextEmailInput.setError(getString(R.string.error_cannot_be_empty));
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        if (password.equals("")) {
            mEditTextPasswordInput.setError(getString(R.string.error_cannot_be_empty));
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAuthService.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void logOut() {
        super.logOut();
        mAuthService.logOut();
    }

    public GoogleApiClient.ConnectionCallbacks getGoogleConnectionCallback() {
        return new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                Logger.d("onConnected");
            }

            @Override
            public void onConnectionSuspended(int i) {
                Logger.d("onConnectionSuspended");
            }
        };
    }

    public GoogleApiClient.OnConnectionFailedListener getGoogleOnConnectionFailedListener() {
        return connectionResult -> Logger.e(connectionResult.toString());
    }
}