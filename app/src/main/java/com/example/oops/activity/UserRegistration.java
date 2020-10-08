package com.example.oops.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.example.oops.EntityClass.RegistrationEntity;
import com.example.oops.R;
import com.example.oops.ResponseClass.RegistrationResponse;
import com.example.oops.ResponseClass.SocialResponse;
import com.example.oops.Utils.AppCommon;
import com.example.oops.Utils.ViewUtils;
import com.example.oops.retrofit.AppService;
import com.example.oops.retrofit.ServiceGenerator;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.exoplayer2.ExoPlayerLibraryInfo.TAG;

public class UserRegistration extends AppCompatActivity {

    @BindView(R.id.ll_user_registration)
    LinearLayout ll_user_registration;
    @BindView(R.id.editTextName)
    EditText editTextName;
    @BindView(R.id.editTextEmailId)
    EditText editTextEmailId;
    @BindView(R.id.editTextCountry)
    EditText editTextCountry;
    @BindView(R.id.editTextPhoneNumber)
    EditText editTextPhoneNumber;
    @BindView(R.id.editTextPassWord)
    EditText editTextPassWord;
    @BindView(R.id.checkBox2)
    CheckBox checkBox2;
    @BindView(R.id.editTextPin)
    EditText editTextPin;
    @BindView(R.id.countrySpinner)
    AppCompatSpinner countrySpinner;
    List<String> spinnerCountryList;
    GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 501;
    CallbackManager callbackManager;

    @BindView(R.id.login_button)
    LoginButton login_button;
    String fireBase;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_registration);
        ButterKnife.bind(this);
        getCountryList();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.i("getInstanceId failed::", task.getException().toString());
                            return;
                        }

                        // Get new Instance ID token
                        fireBase = task.getResult().getToken();
                        Log.i("token::", fireBase);
                    }
                });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        callbackManager = CallbackManager.Factory.create();
    }

    private void getCountryList() {
        spinnerCountryList = new ArrayList<>();
        spinnerCountryList.add("Select Country");
        spinnerCountryList.add("India");
        spinnerCountryList.add("Austrailia");
        spinnerCountryList.add("UK");
        spinnerCountryList.add("US");

        ArrayAdapter<String> spinnerGenderAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, spinnerCountryList);
        countrySpinner.setAdapter(spinnerGenderAdapter);
    }

    @OnClick(R.id.sign_in_button)
    void setSignInButton() {
        signIn();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(GoogleSignInAccount acct) {
        Log.i("data", String.valueOf(acct));
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            callSocialApi(personName, personEmail, personId);
        }
    }

    private void callSocialApi(String personName, String personEmail, String personId) {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            final Dialog dialog = ViewUtils.getProgressBar(UserRegistration.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class);
            Map<String, String> entityMap = new HashMap<>();
            entityMap.put("name", personName);
            entityMap.put("email", personEmail);
            entityMap.put("phone", "");
            entityMap.put("subscriptionType", "free");
            entityMap.put("countryName", "India");
            entityMap.put("socialID", personId);
            entityMap.put("socialAccount", "Gmail");
            entityMap.put("deviceId",fireBase);
            Call call = apiService.socialLogin(entityMap);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(UserRegistration.this).clearNonTouchableFlags(UserRegistration.this);
                    dialog.dismiss();
                    SocialResponse authResponse = (SocialResponse) response.body();

                    if (authResponse != null) {
                        Log.i("Response::", new Gson().toJson(authResponse));
                        if (authResponse.getSuccess() == 200) {

                            if (authResponse.getData().getPin().equals("")) {
                                startActivityForResult(new Intent(UserRegistration.this, EnterPin.class)
                                                .putExtra("data", new Gson().toJson(authResponse.getData()))
                                        , 404);
                            } else {
                                AppCommon.getInstance(UserRegistration.this).setToken(authResponse.getData().getToken());
                                AppCommon.getInstance(UserRegistration.this).setUserLogin(authResponse.getData().getUserId(), true);
                                AppCommon.getInstance(UserRegistration.this).setId(authResponse.getData().getId());
                                AppCommon.getInstance(UserRegistration.this).setSocial(true, "Gmail");
                                AppCommon.getInstance(UserRegistration.this).setUserObject(new Gson().toJson(authResponse.getData()));
                                startActivity(new Intent(UserRegistration.this, Dashboard.class));
                                finishAffinity();
                            }
                            //  callInsertPin();
                            // callLoginApi(new LoginEntity(authResponse.getData().getUserId(), authResponse.getData().getPassword() , fireBase));
                        } else {
                            showSnackbar(ll_user_registration,authResponse.getMsg(),Snackbar.LENGTH_SHORT);
                        }
                    } else {
                        //AppCommon.getInstance(UserRegistration.this).showDialog(UserRegistration.this, authResponse.getMsg());
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(UserRegistration.this).clearNonTouchableFlags(UserRegistration.this);
                    showSnackbar(ll_user_registration,getResources().getString(R.string.ServerError),Snackbar.LENGTH_SHORT);
                }
            });


        } else {
            showSnackbar(ll_user_registration,getResources().getString(R.string.NoInternet),Snackbar.LENGTH_SHORT);
        }
    }


    public void register(View view) {
        String name = editTextName.getText().toString().trim();
        String emailId = editTextEmailId.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String password = editTextPassWord.getText().toString().trim();
        String pintxt = editTextPin.getText().toString().trim();
        if (name.isEmpty())
            editTextName.setError("Please enter user name");
        else if (emailId.isEmpty())
            editTextEmailId.setError("Please enter email id");
        else if (phoneNumber.isEmpty()) {
            editTextPhoneNumber.setError("Please enter phone number");
        } else if (password.isEmpty()) {
            editTextPassWord.setError("Please enter password");
        } else if (pintxt.isEmpty()) {
            editTextPin.setError("Please enter pin");
        } else
            callApi(name, emailId, password, phoneNumber , pintxt);

    }

    private void callApi(String name, String emailId, String password, String phoneNumber , String pin) {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            final Dialog dialog = ViewUtils.getProgressBar(UserRegistration.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class);
            Call call = apiService.RegisterApiCall(new RegistrationEntity(name, emailId, password, phoneNumber, "free" , pin,fireBase));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(UserRegistration.this).clearNonTouchableFlags(UserRegistration.this);
                    dialog.dismiss();
                    RegistrationResponse authResponse = (RegistrationResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Response::", new Gson().toJson(authResponse));
                        if (authResponse.getSuccess() == 200) {
                            AppCommon.getInstance(UserRegistration.this).setToken(authResponse.getData().getToken());
                            AppCommon.getInstance(UserRegistration.this).setUserLogin(authResponse.getData().getUserId(), true);
                            AppCommon.getInstance(UserRegistration.this).setId(authResponse.getData().getId());
                            AppCommon.getInstance(UserRegistration.this).setUserObject(new Gson().toJson(authResponse.getData()));
                            startActivity(new Intent(UserRegistration.this, Dashboard.class));
                            // callLoginApi(new LoginEntity(authResponse.getData().getUserId(), authResponse.getData().getPassword() , fireBase));
                        } else {
                            showSnackbar(ll_user_registration,authResponse.getMsg(),Snackbar.LENGTH_SHORT);
                        }
                    } else {
                        AppCommon.getInstance(UserRegistration.this).showDialog(UserRegistration.this, "Server Error");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(UserRegistration.this).clearNonTouchableFlags(UserRegistration.this);
                    showSnackbar(ll_user_registration,getResources().getString(R.string.ServerError),Snackbar.LENGTH_SHORT);
                }
            });


        } else {
            showSnackbar(ll_user_registration,getResources().getString(R.string.NoInternet),Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
    public void showSnackbar(View view, String message, int duration) {
        Snackbar snackbar = Snackbar.make(view, message, duration);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setBackgroundTint(getResources().getColor(R.color.colorPrimaryDark));
        snackbar.show();
    }

}
