package com.antoniocappiello.cloudapp.service.auth.facebook;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.antoniocappiello.cloudapp.service.auth.AuthProvider;
import com.antoniocappiello.cloudapp.service.auth.AuthProviderBuilder;
import com.antoniocappiello.cloudapp.service.auth.AuthProviderType;
import com.antoniocappiello.cloudapp.service.auth.OAuthTokenHandler;
import com.antoniocappiello.cloudapp.service.event.UpdateCurrentUserEmailEvent;
import com.antoniocappiello.cloudapp.service.utils.AppInfoUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;

public class FacebookAuthProvider implements AuthProvider {

    private final AuthProviderType mAuthProviderType = AuthProviderType.FACEBOOK;
    private final Activity mActivity;

    public CallbackManager mCallbackManager;
    private LoginManager mLoginManager;
    private Boolean isReady = false;
    private View mSignInView;
    private OAuthTokenHandler mOAuthTokenHandler;

    private FacebookAuthProvider(Builder builder) {
        mOAuthTokenHandler = builder.mOAuthTokenHandler;
        mSignInView = builder.mSignInView;
        mActivity = builder.mActivity;

        FacebookSdk.sdkInitialize(mActivity.getApplicationContext());
        initFacebookLoginManager();
        setFacebookSignInView(mSignInView);
        isReady = true;
    }

    private void initFacebookLoginManager() {
        mLoginManager = LoginManager.getInstance();
        mCallbackManager = CallbackManager.Factory.create();

        mLoginManager.registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Logger.d("onSuccess");
                        final AccessToken accessToken = loginResult.getAccessToken();
                        GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                                Logger.d("onCompleted\n" + graphResponse.toString());
                                String email = user.optString("email");
                                String name = user.optString("name");
                                String fbId = user.optString("id");

                                if(email == null || email.isEmpty()){ //if someone signed up for Facebook with a phone number instead of an email address, the email field may be empty.
                                    email = fbId + "@facebookWithNoEmail.com";
                                }

                                AccessToken accessToken = loginResult.getAccessToken();
                                EventBus.getDefault().post(new UpdateCurrentUserEmailEvent(email));
                                if(accessToken != null) {
                                    mOAuthTokenHandler.onOAuthSuccess(accessToken.getToken().toString());
                                }
                                else {
                                    mOAuthTokenHandler.onOAuthFailure("token is null");
                                }
                            }
                        }).executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Logger.e("User closed login dialog.");
                    }

                    @Override
                    public void onError(FacebookException ex) {
                        Logger.e(ex.toString());
                    }
                }
        );
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void logout() {
        mLoginManager.logOut();
    }

    @Override
    public AuthProviderType getType() {
        return mAuthProviderType;
    }

    private void setFacebookSignInView(View facebookSignInView) {
        facebookSignInView.setOnClickListener(v -> login());
    }

    private void login() {
        if (isReady) {
            Collection<String> permissions = Arrays.asList("public_profile", "email");
            mLoginManager.logInWithReadPermissions(mActivity, permissions);
        }
    }

    public static class Builder extends AuthProviderBuilder {
        public FacebookAuthProvider build() {
            if(mActivity == null || mSignInView == null || mOAuthTokenHandler == null){
                throw new IllegalStateException("Builder was not initialized correctly");
            }
            if(!AppInfoUtils.isFacebookAppIdSet(mActivity)){
                throw new IllegalStateException("Facebook app id is not set, check log for details");
            }
            return new FacebookAuthProvider(this);
        }
    }
}
