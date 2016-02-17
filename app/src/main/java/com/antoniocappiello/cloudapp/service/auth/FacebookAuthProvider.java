package com.antoniocappiello.cloudapp.service.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.antoniocappiello.cloudapp.service.backend.BackendAdapter;
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
import com.firebase.ui.auth.core.FirebaseOAuthToken;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;

public class FacebookAuthProvider {

    private final Context mContext;
    public CallbackManager mCallbackManager;
    private LoginManager mLoginManager;
    private Boolean isReady = false;

    public FacebookAuthProvider(Context context, BackendAdapter backendAdapter) {
        mContext = context;
        FacebookSdk.sdkInitialize(context.getApplicationContext());

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

                                AccessToken token = loginResult.getAccessToken();

                                FirebaseOAuthToken foToken = new FirebaseOAuthToken(
                                        "facebook",
                                        token.getToken().toString());

                                backendAdapter.setCurrentUserEmail(email);
                                backendAdapter.authenticateRefWithOAuthFirebasetoken(foToken);
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

        String facebookAppId = "";

        try {
            ApplicationInfo ai = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            facebookAppId = bundle.getString("com.facebook.sdk.ApplicationId");
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NullPointerException e) {
        }

        if (facebookAppId == null) {
            Logger.e("Missing Facebook Application ID, is it set in your AndroidManifest.xml?");
            return;
        }

        if (facebookAppId.compareTo("") == 0) {
            Logger.e("Invalid Facebook Application ID, is it set in your res/values/strings.xml?");
            return;
        }

        isReady = true;

    }

    public void login(Activity activity) {
        if (isReady) {
            Collection<String> permissions = Arrays.asList("public_profile", "email");
            mLoginManager.logInWithReadPermissions(activity, permissions);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void logout() {
        mLoginManager.logOut();
    }
}
