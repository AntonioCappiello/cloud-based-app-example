package com.antoniocappiello.cloudapp.service.backend;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.antoniocappiello.cloudapp.BuildConfig;
import com.antoniocappiello.cloudapp.Constants;
import com.antoniocappiello.cloudapp.R;
import com.antoniocappiello.cloudapp.model.Account;
import com.antoniocappiello.cloudapp.model.Item;
import com.antoniocappiello.cloudapp.model.User;
import com.antoniocappiello.cloudapp.service.action.Action;
import com.antoniocappiello.cloudapp.service.utils.EmailEncoder;
import com.antoniocappiello.cloudapp.ui.screen.itemlist.ItemViewHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.orhanobut.logger.Logger;
import com.pixplicity.easyprefs.library.Prefs;
import com.soikonomakis.rxfirebase.RxFirebase;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;

@Singleton
public class FirebaseBackend implements BackendAdapter<Item> {

    private static final String LIST = "list";
    public static final String FIREBASE_PROPERTY_TIMESTAMP = "timestamp";
    private static final String UID_MAPPING = "uid_mapping";
    private static final String USERS = "users";
    public static final String UID_MAPPINGS = "uidMappings";
    private static final String FIREBASE_PROPERTY_IS_EMAIL_CONFIRMED = "emailConfirmed";

    private Firebase refRoot;
    private Firebase refList;
    private Firebase refUsers;
    private Firebase refUidMapping;

    private FirebaseRecyclerAdapter<Item, ItemViewHolder> mAdapter;
    private Firebase.AuthStateListener mAuthStateListener;

    private String mCurrentUserEmail;

    public FirebaseBackend(Context context) {
        Firebase.setAndroidContext(context);
        refRoot = new Firebase(BuildConfig.FIREBASE_ROOT_URL);
        refList = refRoot.child(LIST);
        refUsers = refRoot.child(USERS);
        refUidMapping = refRoot.child(UID_MAPPING);
        mCurrentUserEmail = Prefs.getString(Constants.KEY_SIGNUP_EMAIL, "");
    }

    @Override
    public void addItemToUserList(Item item) {
        Logger.d(mCurrentUserEmail + "\n" + item.toString());
        if(!isEmailValid() || item == null) {
            throw new InvalidParameterException("Invalid arguments\n" +
                    "user email = " + mCurrentUserEmail +
                    "\nitem = " + item.toString());
        }

        Firebase refUserList = refList.child(EmailEncoder.encodeEmail(mCurrentUserEmail));
        refUserList.push().setValue(item);
    }

    private boolean isEmailValid() {
        return mCurrentUserEmail != null && !mCurrentUserEmail.isEmpty();
    }

    @Override
    public Observable<List<Item>> readItems() {
        return RxFirebase.getInstance()
                .observeValueEvent(refList)
                .map((Func1<DataSnapshot, List<Item>>) snapshot -> {
                    Map<String, Item> map = snapshot.getValue(new GenericTypeIndicator<Map<String, Item>>() {});
                    return new ArrayList<>(map.values());
                });
    }

    @Override
    public RecyclerView.Adapter<ItemViewHolder> getRecyclerViewAdapterForUserItemList() {
        Logger.d(mCurrentUserEmail);
        if(!isEmailValid()) {
            throw new InvalidParameterException("Invalid argument\n" +
                    "user email = " + mCurrentUserEmail);
        }

        Firebase refUserList = refList.child(EmailEncoder.encodeEmail(mCurrentUserEmail));
        if (mAdapter == null) {
            mAdapter = new FirebaseRecyclerAdapter<Item, ItemViewHolder>(Item.class, R.layout.item, ItemViewHolder.class, refUserList) {
                @Override
                public void populateViewHolder(ItemViewHolder itemViewHolder, Item item, int position) {
                    itemViewHolder.updateView(mAdapter.getRef(position).getKey(), item);
                }
            };
        }
        return mAdapter;
    }

    @Override
    public void cleanup() {
        mAdapter.cleanup();
    }

    @Override
    public void addAuthStateListener(Action onAuthenticated, Action onUnAuthenticated) {
        mAuthStateListener = authData -> {
            if (authData == null) {
                Logger.d("unauthenticated");
                if(onUnAuthenticated != null) {
                    onUnAuthenticated.execute();
                }
            }
            else {
                Logger.d("authenticated");
                if(onAuthenticated != null) {
                    onAuthenticated.execute();
                }
            }
        };
        refRoot.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void removeAuthStateListener() {
        if(mAuthStateListener != null) {
            refRoot.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public void signIn(String email, String password, ProgressDialog authProgressDialog, Action onAuthSucceeded, Action onAuthFailed) {
        Firebase.AuthResultHandler authenticationResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Prefs.putString(Constants.KEY_SIGNUP_EMAIL, email);
                mCurrentUserEmail = email;
                authProgressDialog.dismiss();
                onAuthSucceeded.execute();
                ifPasswordIsTemporaryMakeItPermanent(password);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                authProgressDialog.dismiss();
                onAuthFailed.execute(firebaseError.getMessage());
            }
        };
        authProgressDialog.show();
        refRoot.authWithPassword(email, password, authenticationResultHandler);
    }

    private void ifPasswordIsTemporaryMakeItPermanent(String password) {
        getCurrentFirebaseUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    if (!user.isEmailConfirmed()) {
                        Logger.d("Email not confirmed yet");
                        makePasswordPermanent(password);
                    }
                    else {
                        Logger.d("Email already confirmed");
                    }
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Logger.e(firebaseError.toString());
            }
        });
    }

    private Firebase getCurrentFirebaseUserRef() {
        return refUsers.child(EmailEncoder.encodeEmail(mCurrentUserEmail));
    }

    private void makePasswordPermanent(String password) {
        refRoot.changePassword(mCurrentUserEmail, password, password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                setUserEmailConfirmed();
            }
            @Override
            public void onError(FirebaseError firebaseError) {
                Logger.e(firebaseError.toString());
            }
        });
    }

    private void setUserEmailConfirmed() {
        getCurrentFirebaseUserRef()
                .child(FIREBASE_PROPERTY_IS_EMAIL_CONFIRMED)
                .setValue(true);
        Logger.d("Email confirmed");

    }

    @Override
    public void createUser(Account account, ProgressDialog signUpProgressDialog, Action onSignInFailed, Action onSignUpSucceeded) {
        signUpProgressDialog.show();
        refRoot.createUser(account.getUserEmail(), account.getPassword(), new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(final Map<String, Object> result) {
                sendConfirmationEmailWithNewPassword(result, account, signUpProgressDialog, onSignUpSucceeded);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Logger.e(firebaseError.toString());
                signUpProgressDialog.dismiss();
                onSignInFailed.execute(firebaseError.getMessage());
                // TODO handle email adress aready in use, maybe with a reset password screen
            }
        });
    }

    @Override
    public String getCurrentUserEmail() {
        return mCurrentUserEmail;
    }

    @Override
    public void updateItemInUserList(String itemId, Item item) {
        Logger.d("updating " + item.toString());
        if(isEmailValid()) {
            refList.child(EmailEncoder.encodeEmail(mCurrentUserEmail))
                    .child(itemId)
                    .setValue(item);
        }
    }

    private void sendConfirmationEmailWithNewPassword(Map<String, Object> result, Account account, ProgressDialog signUpProgressDialog, Action onSignUpSucceeded) {
        refRoot.resetPassword(account.getUserEmail(), new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                Logger.d("Password reset ok");
                refRoot.authWithPassword(account.getUserEmail(), account.getPassword(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Logger.d("onAuthenticated");
                        signUpProgressDialog.dismiss();
                        Prefs.putString(Constants.KEY_SIGNUP_EMAIL, account.getUserEmail());
                        mCurrentUserEmail = account.getUserEmail();
                        addUidAndUserMapping((String) result.get("uid"), account);
                        onSignUpSucceeded.execute();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Logger.e(firebaseError.getMessage());
                    }
                });

            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Logger.e(firebaseError.toString());
                signUpProgressDialog.dismiss();
            }
        });
    }

    private void addUidAndUserMapping(final String authUserId, Account account) {
        final String encodedEmail = EmailEncoder.encodeEmail(account.getUserEmail());

        HashMap<String, Object> uidAndUserMapping = createUidAndUserMap(encodedEmail, authUserId, account);

        // Try to update the database; if there is already a user, this will fail
        refRoot.updateChildren(uidAndUserMapping, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                Logger.d("user created");
                if (firebaseError != null) {
                    // Try just making a uid mapping
                    refUidMapping.child(authUserId).setValue(encodedEmail);
                }
                logOut();
            }
        });

    }

    private HashMap<String, Object> createUidAndUserMap(String encodedEmail, String authUserId, Account account) {
        HashMap<String, Object> uidAndUserMapping = new HashMap<String, Object>();
        uidAndUserMapping.put("/" + USERS + "/" + encodedEmail, createUserMap(account));
        uidAndUserMapping.put("/" + UID_MAPPINGS + "/" + authUserId, encodedEmail);
        return uidAndUserMapping;
    }

    private HashMap<String, Object> createUserMap(Account account) {
        final String encodedEmail = EmailEncoder.encodeEmail(account.getUserEmail());
        User newUser = new User(account.getUserName(), encodedEmail, createTimestampJoinedMap());
        HashMap<String, Object> newUserMap = (HashMap<String, Object>) new ObjectMapper().convertValue(newUser, Map.class);
        return newUserMap;
    }

    private HashMap<String, Object> createTimestampJoinedMap() {
        HashMap<String, Object> timestampJoined = new HashMap<>();
        timestampJoined.put(FirebaseBackend.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        return timestampJoined;
    }

    @Override
    public void logOut() {
        refRoot.unauth();
    }

}

