package com.example.oops.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oops.R;

public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = WebViewActivity.class.getSimpleName();
    public static String API_BASE_URL_SUBSCRIBE = "http://15.207.175.218/restPhp/index.php/Cashfree/addMoney/?";

    WebView myWebView;
    ImageView iv_back;
    TextView tv_title_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Bundle bundle = getIntent().getExtras();
        String userId = bundle.getString("userId");
        String orderId = bundle.getString("orderId");
        String orderAmount = bundle.getString("orderAmount");
        String customerName = bundle.getString("customerName");
        String customerPhone = bundle.getString("customerPhone");
        String customerEmail = bundle.getString("customerEmail");
        String plan_details_id = bundle.getString("plan_details_id");

        String url = API_BASE_URL_SUBSCRIBE+"customerPhone="+customerPhone
                +"&orderAmount="+orderAmount
                +"&orderId="+orderId
                +"&customerEmail="+customerEmail
                +"&userId="+userId
                +"&customerName="+customerName
                +"&plan_details_id="+plan_details_id;

        Log.d(TAG,"url : "+url);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_title_left = (TextView) findViewById(R.id.tv_title_left);
        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl(url);

        myWebView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog = new ProgressDialog(WebViewActivity.this);

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressDialog.setTitle("Loading...");
                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }

        });
    }

    @Override
    public void onBackPressed() {
        if (handleOnBackPress()) return;
        super.onBackPressed();
    }

    private boolean handleOnBackPress() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        return false;
    }

}