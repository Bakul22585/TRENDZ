package com.example.trendz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText FullName, Phone, SponsorID, SponsorName, Email, Password, ConfirmPassword, Activation;
    TextView FullNameError, PhoneError, SponsorIDError, EmailError, PasswordError, ConfirmPasswordError, ActivationError;
    Button Register;
    String ActivationCode;
    Boolean ActivationStatus = true;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FullName = findViewById(R.id.TxtFullName);
        FullNameError = findViewById(R.id.FullNameErrorView);
        Phone = findViewById(R.id.TxtPhone);
        PhoneError = findViewById(R.id.PhoneErrorView);
        SponsorID = findViewById(R.id.TxtSponsorId);
        SponsorIDError = findViewById(R.id.SponsorIdErrorView);
        SponsorName = findViewById(R.id.TxtSponsorName);
        Email = findViewById(R.id.TxtEmail);
        EmailError = findViewById(R.id.EmailErrorView);
        Password = findViewById(R.id.TxtPassword);
        PasswordError = findViewById(R.id.PasswordErrorView);
        ConfirmPassword = findViewById(R.id.TxtConfirmPassword);
        ConfirmPasswordError = findViewById(R.id.ConfirmPasswordErrorView);
        Register = findViewById(R.id.BtnRegister);
        Activation = findViewById(R.id.TxtActivation);
        ActivationError = findViewById(R.id.ActivationErrorView);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(String.valueOf(R.string.banner_unit));

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this, R.style.DialogTheme);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message

        SponsorID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!SponsorID.getText().toString().equals("")) {
                    progressDialog.show(); // show progress dialog
                    RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                    String URL = "http://restrictionsolution.com/ci/trendz_world/user/details?sponsor=" + SponsorID.getText().toString();

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getBoolean("success")) {
                                        JSONArray UserData = jsonObject.getJSONArray("data");
                                        JSONObject UserDetails = (JSONObject) UserData.get(0);
                                        SponsorName.setText(UserDetails.getString("fullname"));
                                    } else {
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                        SponsorID.setText("");
                                        SponsorName.setText("");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }
                    );
                    requestQueue.add(stringRequest);
                }
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register.setEnabled(false);
                Boolean successBoolean = true;
                if (FullName.getText().toString().equals("")) {
                    successBoolean = false;
                    FullNameError.setVisibility(View.VISIBLE);
                }
                if (!FullName.getText().toString().equals("")) {
                    FullNameError.setVisibility(View.GONE);
                }

                if (Phone.getText().toString().equals("")) {
                    successBoolean = false;
                    PhoneError.setVisibility(View.VISIBLE);
                }
                if (!Phone.getText().toString().equals("")) {
                    PhoneError.setVisibility(View.GONE);
                }

                if (SponsorID.getText().toString().equals("")) {
                    successBoolean = false;
                    SponsorIDError.setVisibility(View.VISIBLE);
                }
                if (!SponsorID.getText().toString().equals("")) {
                    SponsorIDError.setVisibility(View.GONE);
                }

                if (Email.getText().toString().equals("")) {
                    successBoolean = false;
                    EmailError.setVisibility(View.VISIBLE);
                }
                if (!Email.getText().toString().equals("")) {
                    EmailError.setVisibility(View.GONE);
                }

                if (Password.getText().toString().equals("")) {
                    successBoolean = false;
                    PasswordError.setVisibility(View.VISIBLE);
                }
                if (!Password.getText().toString().equals("")) {
                    PasswordError.setVisibility(View.GONE);
                }

                if (ConfirmPassword.getText().toString().equals("")) {
                    successBoolean = false;
                    ConfirmPasswordError.setText("Enter Confirmation Password");
                    ConfirmPasswordError.setVisibility(View.VISIBLE);
                }
                if (!ConfirmPassword.getText().toString().equals("")) {
                    ConfirmPasswordError.setVisibility(View.GONE);
                }

                if (!ConfirmPassword.getText().toString().equals(Password.getText().toString())) {
                    successBoolean = false;
                    ConfirmPasswordError.setText("Confirmation password must be the same as password");
                    ConfirmPasswordError.setVisibility(View.VISIBLE);
                }

                if (Activation.getText().toString().equals("") && ActivationStatus.equals(false)) {
                    successBoolean = false;
                    ActivationError.setText("Enter your email activation code");
                    ActivationError.setVisibility(View.VISIBLE);
                }

                if (!Activation.getText().toString().equals("") && ActivationStatus.equals(false)) {
                    ActivationError.setVisibility(View.GONE);
                }

                if (!Activation.getText().toString().equals(ActivationCode) && ActivationStatus.equals(false)) {
                    successBoolean = false;
                    ActivationError.setText("Your email code and activation code do not match, Please check and try again");
                    ActivationError.setVisibility(View.VISIBLE);
                }

                if(successBoolean){
                    RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                    String URL = "http://restrictionsolution.com/ci/trendz_world/user/add";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject responseData = new JSONObject(response);
                                if (responseData.getBoolean("success")) {
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(), responseData.getString("message"), Toast.LENGTH_SHORT).show();
                                    if (ActivationStatus.equals(true)) {
                                        Register.setEnabled(true);
                                        ActivationStatus = false;
                                        Activation.setVisibility(View.VISIBLE);
                                        ActivationCode = responseData.getString("activation");
                                        Register.setText("Complete");
                                    }
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
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("fullname", FullName.getText().toString());
                            params.put("mobile", Phone.getText().toString());
                            params.put("sponsor", SponsorID.getText().toString());
                            params.put("email", Email.getText().toString());
                            params.put("password", Password.getText().toString());
                            params.put("activation", String.valueOf(ActivationStatus));
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }
            }
        });
    }
}