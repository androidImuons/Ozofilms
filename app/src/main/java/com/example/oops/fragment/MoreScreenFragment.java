package com.example.oops.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.oops.EntityClass.LogoutEntity;
import com.example.oops.EntityClass.SupportHelpEntity;
import com.example.oops.R;
import com.example.oops.ResponseClass.RegistrationResponse;
import com.example.oops.Utils.AppCommon;
import com.example.oops.Utils.ViewUtils;
import com.example.oops.activity.Login;
import com.example.oops.activity.Support_Help;
import com.example.oops.retrofit.AppService;
import com.example.oops.retrofit.ServiceGenerator;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoreScreenFragment extends Fragment {
    @BindView(R.id.txtLogout)
    AppCompatTextView txtLogout;

    public MoreScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.morescreen_fragment, container, false);
        ButterKnife.bind(this, view);
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();

            }
        });
        return view;

    }

    private void logoutUser() {

            if (AppCommon.getInstance(getActivity()).isConnectingToInternet(getActivity())) {
                final Dialog dialog = ViewUtils.getProgressBar(getActivity());
                AppCommon.getInstance(getActivity()).setNonTouchableFlags(getActivity());
                AppService apiService = ServiceGenerator.createService(AppService.class);
//            Change
                Call call = apiService.LogoutApiCall(new LogoutEntity(AppCommon.getInstance(getActivity()).getToken()));
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        AppCommon.getInstance(MoreScreenFragment.this.getContext()).clearNonTouchableFlags(MoreScreenFragment.this.getActivity());
                        dialog.dismiss();
                        RegistrationResponse authResponse = (RegistrationResponse) response.body();
                        if (authResponse != null) {
                            Log.i("Response::", new Gson().toJson(authResponse));
                            if (authResponse.getSuccess() == 200) {
                                AppCommon.getInstance(getActivity()).clearPreference();
                                startActivity(new Intent(getActivity(), Login.class));
                                getActivity().finishAffinity();
                                Toast.makeText(getActivity(), "Logout Successfully", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getActivity(), authResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            AppCommon.getInstance(MoreScreenFragment.this.getContext()).showDialog(MoreScreenFragment.this.getActivity(), "Server Error");
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        dialog.dismiss();
                        AppCommon.getInstance(MoreScreenFragment.this.getContext()).clearNonTouchableFlags(MoreScreenFragment.this.getActivity());
                        // loaderView.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                    }
                });


            } else {
                // no internet
                Toast.makeText(getActivity(), "Please check your internet", Toast.LENGTH_SHORT).show();
            }
        }


}