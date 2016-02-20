/*
 * Created by Antonio Cappiello on 2/20/16 12:32 PM
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 2/18/16 5:40 PM
 */

package com.antoniocappiello.socialauth.provider.twitter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.antoniocappiello.socialauth.Response;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterPromptActivity extends Activity {

    private Twitter mTwitter;
    private WebView mTwitterView;

    @Override
    public void onBackPressed() {
        sendResultError(TwitterActions.USER_ERROR, Response.LOGIN_CANCELLED.ordinal(), "User closed login prompt.");
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String twitterKey = "";
        String twitterSecret = "";

        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            twitterKey = bundle.getString("com.antoniocappiello.cloudapp.TwitterKey");
            twitterSecret = bundle.getString("com.antoniocappiello.cloudapp.TwitterSecret");
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NullPointerException e) {}

        if (twitterKey == null || twitterSecret == null) {
            sendResultError(TwitterActions.PROVIDER_ERROR, Response.MISSING_PROVIDER_APP_KEY.ordinal(), "Missing Twitter key/secret, are they set in your AndroidManifest.xml?");
            return;
        }

        if (twitterKey.compareTo("") == 0|| twitterSecret.compareTo("") == 0) {
            sendResultError(TwitterActions.PROVIDER_ERROR, Response.INVALID_PROVIDER_APP_KEY.ordinal(), "Invalid Twitter key/secret, are they set in your res/values/strings.xml?");
            return;
        }

        mTwitter = new TwitterFactory(new ConfigurationBuilder()
                .setOAuthConsumerKey(twitterKey)
                .setOAuthConsumerSecret(twitterSecret)
                .build()).getInstance();

        // setup ic_twitter webview
        mTwitterView = new WebView(this);
        mTwitterView.getSettings().setJavaScriptEnabled(true);

        // initialize view
        setContentView(mTwitterView);

        // fetch the oauth request token then prompt the user to authorize the application
        new AsyncTask<Void, Void, RequestToken>() {
            @Override
            protected RequestToken doInBackground(Void... params) {
                RequestToken token = null;
                try {
                    token = mTwitter.getOAuthRequestToken("oauth://cb");
                } catch (TwitterException te) {
                    sendResultError(TwitterActions.PROVIDER_ERROR, Response.MISC_PROVIDER_ERROR.ordinal(), te.toString());
                }
                return token;
            }

            @Override
            protected void onPostExecute(final RequestToken token) {
                if (token == null) {
                    // Then doInBackground failed and triggered error, so just return
                    return;
                } else {
                    mTwitterView.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(final WebView view, final String url) {
                            if (url.startsWith("oauth://cb")) {
                                mTwitterView.destroy();
                                if (url.contains("oauth_verifier")) {
                                    getTwitterOAuthTokenAndLogin(token, Uri.parse(url).getQueryParameter("oauth_verifier"));
                                } else if (url.contains("denied")) {
                                    sendResultError(TwitterActions.USER_ERROR, Response.LOGIN_CANCELLED.ordinal(), "User denied access to their account.");
                                }
                            }
                        }
                    });
                    mTwitterView.loadUrl(token.getAuthorizationURL());
                }
            }
        }.execute();
    }

    private void getTwitterOAuthTokenAndLogin(final RequestToken requestToken, final String oauthVerifier) {
        new AsyncTask<Void, Void, AccessToken>() {
            @Override
            protected AccessToken doInBackground(Void... params) {
                AccessToken accessToken = null;
                try {
                    accessToken = mTwitter.getOAuthAccessToken(requestToken, oauthVerifier);
                } catch (TwitterException te) {
                    sendResultError(TwitterActions.PROVIDER_ERROR, Response.MISC_PROVIDER_ERROR.ordinal(), te.toString());
                }
                return accessToken;
            }

            @Override
            protected void onPostExecute(AccessToken token) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("oauth_token", token.getToken());
                resultIntent.putExtra("oauth_token_secret", token.getTokenSecret());
                resultIntent.putExtra("user_id", token.getUserId() + "");
                setResult(TwitterActions.SUCCESS, resultIntent);
                finish();
            }
        }.execute();
    }

    private void sendResultError(Integer status, int errCode, String err) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("error", err);
        resultIntent.putExtra("code", errCode);
        setResult(status, resultIntent);
        finish();
    }
}