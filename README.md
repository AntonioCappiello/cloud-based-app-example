# Cloud-Based App Example

This is a sample app based on a replaceable backend. 

![alt text](http://blog.raremile.com/wp-content/uploads/2014/07/Baas.png)

The app uses the cloud data via a [BackendAdapter](https://github.com/AntonioCappiello/cloud-based-app-example/blob/master/app/src/main/java/com/antoniocappiello/cloudapp/presenter/backend/BackendAdapter.java) Interface which can be implemented with the backend (BaaS) that you prefer the most.

For the sake of this example I have implemented the backend in [Firebase](https://www.firebase.com/),
partially using as inspiration the Udacity's course ["Firebase Essentials For Android"](https://www.udacity.com/course/firebase-essentials-for-android--ud009) by Google.

![alt text](http://blog.ionic.io/wp-content/uploads/2015/06/firebase-ionic-user-auth.png)

### Instruction to build the project
The app's *build.gradle* files uses three variables to connect to your firebase app. Those variables are not included in the git repo, therefore you need to create a gradle.properties file in the root folder of the project with the following variables:
```javascript
FirebaseRootUrl = "https://[YOUR FIREBASE APP NAME AS IN THE DASHBORAD].firebaseio.com/"
FirebaseTestEmail = "[yourtestmail@mail.com]"
FirebaseTestPw = "[yourtestpw]"
```
The *FirebaseTestEmail* and *FirebaseTestPw* are used only to facilitate the login to Firebase with a known account and to avoid to fill in the username and password. Those two variables are used only in the *dev* product flavor.

### LogIn via Facebook, Twitter

In order to use authentication via social media, create under `res/values` a file named `keys.xml` (it is already in .gitignore) and add to it the following lines with your data:

```xml
<string name="facebook_app_id">[VALUE]</string>
<string name="twitter_app_key">[VALUE]</string>
<string name="twitter_app_secret">[VALUE]</string>
```

### LogIn via Google

If you're using Google authentication, place your `google-services.json` in the app folder.
