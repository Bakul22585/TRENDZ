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

public class RestpasswordActivity extends AppCompatActivity {

    private AdView mAdView;
    EditText RestCode, NewPassword, RestCodeConfirmPassword;
    TextView RestCodeError, NewPasswordError, RestCodeConfirmPasswordError;
    Button RestPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restpassword);

        RestCode = findViewById(R.id.TxtRestPasswordCode);
        RestCodeError = findViewById(R.id.RestPasswordCodeErrorView);
        NewPassword = findViewById(R.id.TxtNewPassword);
        NewPasswordError = findViewById(R.id.NewPasswordErrorView);
        RestCodeConfirmPassword = findViewById(R.id.TxtRestCodePassword);
        RestCodeConfirmPasswordError = findViewById(R.id.RestCodeConfirmPasswordView);
        RestPassword = findViewById(R.id.btnRestPassword);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(String.valueOf(R.string.banner_unit));

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.DialogTheme);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView4);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        RestPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean successBoolean = true;
                if (RestCode.getText().toString().equals("")) {
                    successBoolean = false;
                    RestCodeError.setVisibility(View.VISIBLE);
                }
                if (!RestCode.getText().toString().equals("")) {
                    RestCodeError.setVisibility(View.GONE);
                }

                if (NewPassword.getText().toString().equals("")) {
                    successBoolean = false;
                    NewPasswordError.setVisibility(View.VISIBLE);
                }
                if (!NewPassword.getText().toString().equals("")) {
                    NewPasswordError.setVisibility(View.GONE);
                }

                if (RestCodeConfirmPassword.getText().toString().equals("")) {
                    RestCodeConfirmPasswordError.setText("Enter Confirm Password");
                    successBoolean = false;
                    RestCodeConfirmPasswordError.setVisibility(View.VISIBLE);
                }
                if (!RestCodeConfirmPassword.getText().toString().equals("")) {
                    RestCodeConfirmPasswordError.setVisibility(View.GONE);
                }

                if (!NewPassword.getText().toString().equals(RestCodeConfirmPassword.getText().toString())) {
                    RestCodeConfirmPasswordError.setText("Confirmation password must be the same as password");
                    successBoolean = false;
                    RestCodeConfirmPasswordError.setVisibility(View.VISIBLE);
                }

                if (!RestCode.getText().toString().equals("") && !NewPassword.getText().toString().equals("") && !RestCodeConfirmPassword.getText().toString().equals("") && !NewPassword.getText().toString().equals(RestCodeConfirmPassword.getText().toString())) {

                }

                if (successBoolean) {
                    progressDialog.show();
                    RequestQueue requestQueue = Volley.newRequestQueue(RestpasswordActivity.this);
                    String URL = "http://restrictionsolution.com/ci/trendz_world/user/restpassword";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject responseData = new JSONObject(response);

                                if (responseData.getBoolean("success")) {
                                    Toast.makeText(getApplicationContext(), responseData.getString("message"), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RestpasswordActivity.this, MainActivity.class);
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
                            params.put("code", RestCode.getText().toString());
                            params.put("password", NewPassword.getText().toString());

                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }
            }
        });
    }
}