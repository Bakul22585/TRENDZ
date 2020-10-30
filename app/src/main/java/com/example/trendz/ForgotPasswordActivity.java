package com.example.trendz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {

    private AdView mAdView;
    EditText ForgotPassword;
    TextView ForgotPasswordError, AlreadyCodeView;
    Button ReceiveCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ForgotPassword = findViewById(R.id.txtForgotPassword);
        ForgotPasswordError = findViewById(R.id.ForgotPasswordErrorView);
        ReceiveCode = findViewById(R.id.btnSendmail);
        AlreadyCodeView = findViewById(R.id.AlreadyCodeView);

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.DialogTheme);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(String.valueOf(R.string.banner_unit));

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView3);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        AlreadyCodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, RestpasswordActivity.class);
                startActivity(intent);
            }
        });

        ReceiveCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean successBoolean = true;
                if (ForgotPassword.getText().toString().equals("")) {
                    successBoolean = false;
                    ForgotPasswordError.setVisibility(View.VISIBLE);
                }
                if (!ForgotPassword.getText().toString().equals("")) {
                    ForgotPasswordError.setVisibility(View.GONE);
                }
                if (successBoolean) {
                    progressDialog.show();
                    RequestQueue requestQueue = Volley.newRequestQueue(ForgotPasswordActivity.this);
                    String URL = "http://restrictionsolution.com/ci/trendz_world/user/forgotpassword";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject responseData = new JSONObject(response);

                                if (responseData.getBoolean("success")) {
                                    Toast.makeText(getApplicationContext(), responseData.getString("message"), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ForgotPasswordActivity.this, RestpasswordActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), responseData.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            final Map<String, String> headers = new HashMap<>();
                            headers.put("Content-Type", "application/x-www-form-urlencoded");

                            return headers;
                        }

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("email", ForgotPassword.getText().toString());

                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }
            }
        });
    }
}