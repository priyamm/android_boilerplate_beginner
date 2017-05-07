package com.example.priyamgupta.android_boilerplate.screens.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priyamgupta.android_boilerplate.models.Repo;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import com.example.priyamgupta.android_boilerplate.R;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends FragmentActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;
    CallbackManager callbackManager;
    GoogleApiClient mGoogleApiClient;

    private ProgressDialog mProgressDialog;
    @BindView(R.id.login_button)
    LoginButton loginButton;
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.sign_in_button)
    SignInButton signInButton;
    @BindView(R.id.sign_out_button)
    SignInButton signOutButton;
    @BindView(R.id.disconnect_button)
    SignInButton signdisButton;
    @BindView(R.id.imageView)
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        loginButton.setReadPermissions("email");

//        ArrayList<Repo> list = getIntent().getExtras().getParcelableArrayList("LIST");
//        for(Repo repo : list)
//            Log.d("TAG" , repo.toString());
//        if(!list.isEmpty())
//            Log.d("TAG", "not" + list.size());
//        Gson gson = new Gson();
//        Log.d("TAG",gson.toJson(list));

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo("com.package.mypackage", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String sign= Base64.encodeToString(md.digest(), Base64.DEFAULT);
//                Log.d("MY KEY HASH:", sign);
//                Toast.makeText(getApplicationContext(),sign,         Toast.LENGTH_LONG).show();
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//        } catch (NoSuchAlgorithmException e) {
//        }

        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Timber.plant(new Timber.DebugTree());
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                Timber.d("FB : AccessToken %s", loginResult.getAccessToken().getToken());
                Gson gson = new Gson();
                String json = gson.toJson(loginResult.getAccessToken());
                Timber.i("FB : %s", json);
                textView1.setText(loginResult.getAccessToken().getToken());
                Timber.d("FB : Granted %s", loginResult.getRecentlyGrantedPermissions());
                textView2.setText(loginResult.getRecentlyGrantedPermissions().toString());
                Timber.d("FB : Permitted %s", loginResult.getRecentlyDeniedPermissions());
                textView3.setText(loginResult.getRecentlyDeniedPermissions().toString());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode("469596530589-mca27j8adrv47armolcdt64bi54qf2rc.apps.googleusercontent.com", true)
                .requestEmail()

                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);
        signOutButton.setSize(SignInButton.SIZE_STANDARD);
        signOutButton.setOnClickListener(this);
        signdisButton.setSize(SignInButton.SIZE_STANDARD);
        signdisButton.setOnClickListener(this);
        imageView.setOnClickListener(this);


    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Timber.d("onActivityResult : ");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            return;
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        Gson gson = new Gson();
        String json = gson.toJson(result);
        Log.d("GOOGLE : %s", json);
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            json = gson.toJson(acct);
            Timber.d("GOOGLE : %s", json);
            textView2.setText("Result : Name" + acct.getDisplayName() + "Email:" +
                    acct.getEmail() + "Id:" + acct.getId() + "Id Token:" + acct.getIdToken() + "Server auth code:" +
                    acct.getServerAuthCode() + "Account:" + acct.getAccount());
            Log.d("Result : Name",  acct.getDisplayName() + "Email:" +
                    acct.getEmail() + "Id:" + acct.getId() + "Id Token:" + acct.getIdToken() + "Server auth code:" +
                    acct.getServerAuthCode() + "Account:" + acct.getAccount());
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]


    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
            textView1.setText("SIGNEDIN");

        } else {
            textView1.setText("SIGNOUT");
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                Timber.d("sign_in_button");
                signIn();
                Timber.d("sign_in_button 2");
                break;
            case R.id.sign_out_button:
                Timber.d("sign_out_button");
                signOut();
                break;
            case R.id.disconnect_button:
                Timber.d("disconnect_button");
                revokeAccess();
                break;
            case R.id.imageView:
                Intent i = new Intent(this, NavActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.e("Failed");

    }
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("LoAD..");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
}
