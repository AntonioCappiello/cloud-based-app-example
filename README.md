# Cloud-Based App Example

This is a sample app which shows:

1. how to make an app decoupled by its backend, thanks to a **BackendAdapter** interface;
2. and how to integrate the most common social media authentication providers, like Facebook, Twitter and Google, via the **Social Auth** library.

![alt text](http://blog.raremile.com/wp-content/uploads/2014/07/Baas.png)

![alt text](http://blog.ionic.io/wp-content/uploads/2015/06/firebase-ionic-user-auth.png)

For the sake of this example I have implemented the backend in [Firebase](https://www.firebase.com/),
partially using as inspiration the Udacity's course ["Firebase Essentials For Android"](https://www.udacity.com/course/firebase-essentials-for-android--ud009) by Google.

## Instruction to build the project
The app's *build.gradle* files uses three variables to connect to your firebase app. Those variables are not included in the git repo for security reasons, therefore you need to create a gradle.properties file in the root folder of the project with the following variables:
```javascript
FirebaseRootUrl = "https://[YOUR FIREBASE APP NAME AS IN THE DASHBORAD].firebaseio.com/"
FirebaseTestEmail = "[yourtestmail@mail.com]"
FirebaseTestPw = "[yourtestpw]"
```
The *FirebaseTestEmail* and *FirebaseTestPw* are used only to facilitate the login to Firebase with a known account and to avoid to fill in the username and password. Those two variables are used only in the *dev* product flavor.

## Social Authentication service (libray)

To facilitate the authentication flow with Facebook, Twitter, and Google, this cloud-based-app sample uses the [*Social Auth*](https://bintray.com/antoniocappiello/maven/socialauth/) library. This library is [open-source](https://github.com/AntonioCappiello/cloud-based-app-example/tree/feature/convert_auth_package_in_library/socialauth/src/main/java/com/antoniocappiello/socialauth) and is partially inspired on the [Firebase-UI library](https://github.com/firebase/FirebaseUI-Android). The Firebase-UI library provides login to social media only via a predefined pop-up dialog and it doesn't help to transfer the authentication information and the current firebase ref from one activity to another of our app (in case we are building a multi-activity app).
**The *Social Auth* library**, instead, **allows you to use your custom UI to login to social media, and it is decoupled from your backend implementation thanks to the BackendAdapter interface**. For example, if you decide to use Firebase as your backend, you can create a FirebaseBackend class which extends the BackendAdapter and which is available in your whole app, by removing the need to transfer *refs* and *auth* information among activities. For more information, see the could-based-app example of this repo.

### Social Authentication service usage

The Social Auth library is available on jCenter, therefore, it could easily be imported into your app with the following dependency:

```xml
  compile 'com.antoniocappiello.library:socialauth:0.0.1'
```

To start using it, just create a new instance and add one or more social media provider:

```java
  mAuthService = new AuthService()
                  .enableAuthProvider(googleAuthProvider)
                  .enableAuthProvider(facebookAuthProvider)
                  .enableAuthProvider(twitterAuthProvider);
```

You can create social media provider in this way:

```java
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
```

As you can see, each authentication provider can be built with a *View* on which will be automatically associated the "Sign In" action.

When the authentication with the specific provider is completed succesfully, one of the methods of the [BackendAdapter](https://github.com/AntonioCappiello/cloud-based-app-example/blob/feature/convert_auth_package_in_library/socialauth/src/main/java/com/antoniocappiello/socialauth/BackendAdapter.java) interface will be invoked. You can use them to trigger UI changes because the user are logged in, or you can add a further step of authentication by using the returned token, in case your backend needs it (like in this sample project where it is used to automatically authenticate to Firebase).

### LogIn via Facebook and Twitter

In order to use authentication via social media, create under `res/values` a file named `keys.xml` (it is already in .gitignore) and add to it the following lines with your data:

```xml
<string name="facebook_app_id">[VALUE]</string>
<string name="twitter_app_key">[VALUE]</string>
<string name="twitter_app_secret">[VALUE]</string>
```

Moreover, for Facebook you need also to configure your app in your Facebook Dev page, as explained [here](https://www.firebase.com/docs/android/guide/login/facebook.html#section-configure) (don't forget to add your app keyhash, which you can generate with the method `service/utils/LogHelper.logHashKey()`).

For Twitter, follow those [instructions](https://www.firebase.com/docs/android/guide/login/twitter.html).

### LogIn via Google

If you're using Google authentication, you need to create a google project in your developer portal by following those [instructions](https://www.firebase.com/docs/android/guide/login/google.html) and then place your `google-services.json` in the app folder.
