package com.example.oops.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import com.example.oops.EntityClass.LogoutEntity;
import com.example.oops.Ooops;
import com.example.oops.R;
import com.example.oops.ResponseClass.LogoutResponse;
import com.example.oops.Utils.AppCommon;
import com.example.oops.Utils.ViewUtils;
import com.example.oops.activity.AppSetting;
import com.example.oops.activity.LegalActivity;
import com.example.oops.activity.Login;
import com.example.oops.activity.SubscriptionActivity;
import com.example.oops.activity.Support_Help;
import com.example.oops.data.databasevideodownload.DatabaseClient;
import com.example.oops.retrofit.AppService;
import com.example.oops.retrofit.ServiceGenerator;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoreScreenFragment extends Fragment {

    @BindView(R.id.sv_MoreOptions)
    ScrollView sv_MoreOptions;
    @BindView(R.id.txtLogout)
    AppCompatTextView txtLogout;
    @BindView(R.id.txtAppSetting)
    AppCompatTextView txtAppSetting;
    @BindView(R.id.txtSupportHelp)
    AppCompatTextView txtSupportHelp;
    @BindView(R.id.txtLegal)
    AppCompatTextView txtLegal;
    @BindView(R.id.txtVersion)
    AppCompatTextView txtVersion;

    public MoreScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.morescreen_fragment, container, false);
        ButterKnife.bind(this, view);
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            txtVersion.setText("Version " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();

            }
        });

        return view;

    }

    @OnClick(R.id.txtAppSetting)
    public void setTxtAppSetting() {
        startActivity(new Intent(getActivity(), AppSetting.class));
    }

    @OnClick(R.id.txtSupportHelp)
    public void setTxtSupportHelp() {
        startActivity(new Intent(getActivity(), Support_Help.class));
    }

    @OnClick(R.id.txtSub)
    public void setSubscription() {
        startActivity(new Intent(getActivity(), SubscriptionActivity.class));
    }

    @OnClick(R.id.txtLegal)
    public void setTxtLegal() {
        startActivity(new Intent(getActivity(), LegalActivity.class));
    }

    private void logoutUser() {


        AlertDialog.Builder adb = new AlertDialog.Builder(getContext(),R.style.MyDialogTheme1);
        adb.setTitle(getResources().getString(R.string.app_name));
        adb.setIcon(R.mipmap.ic_launcher_round);
        adb.setTitle( Html.fromHtml("<font color='#FFFFFF'>Logout </font>"));
        adb.setMessage( Html.fromHtml("<font color='#FFFFFF'>Are you sure you want to logout? </font>"));
       // adb.setMessage(getResources().getString(R.string.r_u_sure_logout_message));
        adb.setPositiveButton(getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        callApi();
                        //startActivity(new Intent());
                        // finishAffinity();
                    }

                });
        adb.setNegativeButton(getString(R.string.no_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.show();

    }

    private void callApi() {
        // user login with google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
       GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
       mGoogleSignInClient.signOut();

        if (AppCommon.getInstance(getActivity()).isConnectingToInternet(getActivity())) {
            final Dialog dialog = ViewUtils.getProgressBar(getActivity());
            AppCommon.getInstance(getActivity()).setNonTouchableFlags(getActivity());
            AppService apiService = ServiceGenerator.createService(AppService.class);
            Call call = apiService.LogoutApiCall(new LogoutEntity(AppCommon.getInstance(getActivity()).getId(), AppCommon.getInstance(getActivity()).getUserId()));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(MoreScreenFragment.this.getContext()).clearNonTouchableFlags(MoreScreenFragment.this.getActivity());
                    dialog.dismiss();
                    LogoutResponse authResponse = (LogoutResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Response::", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            AppCommon.getInstance(getActivity()).clearPreference();
                            Ooops.getInstance().getDownloadManager().removeAllDownloads();
                            DatabaseClient.getInstance(getContext()).getAppDatabase().videoDownloadDao().nukeTable();
                            startActivity(new Intent(getActivity(), Login.class));
                            getActivity().finishAffinity();
                            showSnackbar(sv_MoreOptions,getResources().getString(R.string.Logout),Snackbar.LENGTH_SHORT);
                        } else {
                            showSnackbar(sv_MoreOptions,authResponse.getMessage(),Snackbar.LENGTH_SHORT);
                        }
                    } else {
                        AppCommon.getInstance(MoreScreenFragment.this.getContext()).showDialog(MoreScreenFragment.this.getActivity(), "Server Error");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(MoreScreenFragment.this.getContext()).clearNonTouchableFlags(MoreScreenFragment.this.getActivity());
                    showSnackbar(sv_MoreOptions,getResources().getString(R.string.ServerError),Snackbar.LENGTH_SHORT);
                }
            });


        } else {
            showSnackbar(sv_MoreOptions,getResources().getString(R.string.NoInternet),Snackbar.LENGTH_SHORT);
        }
    }

    public void showSnackbar(View view, String message, int duration)
    {
        Snackbar snackbar= Snackbar.make(view, message, duration);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setBackgroundTint(getResources().getColor(R.color.colorPrimaryDark));
        snackbar.show();
    }


}