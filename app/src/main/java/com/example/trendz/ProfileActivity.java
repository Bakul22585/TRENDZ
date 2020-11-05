package com.example.trendz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    EditText FullName, Mobile, Password, ConfirmPassword;
    TextView FullNameError, MobileError, PasswordError, ConfirmPasswordError;
    CheckBox EditPassword;
    Button Update;
    String LoginUserId;
    private AdView mAdView;
    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Profile</font>"));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        SessionManagement sessionManagement = new SessionManagement(ProfileActivity.this);

        FullName = findViewById(R.id.editTextProfileFullName);
        FullNameError = findViewById(R.id.profileFullNameError);
        Mobile = findViewById(R.id.editTextProfileMobile);
        MobileError = findViewById(R.id.profileMobileError);
        Password = findViewById(R.id.editTextProfilePassword);
        PasswordError = findViewById(R.id.profilePasswordError);
        ConfirmPassword = findViewById(R.id.editTextProfileConfirmPassword);
        ConfirmPasswordError = findViewById(R.id.profileConfirmPasswordError);
        EditPassword = findViewById(R.id.profileChangePasswordCheckbox);
        Update = findViewById(R.id.btnProfileUpdate);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(String.valueOf(R.string.banner_unit));

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        this.rewardedAd = new RewardedAd(this, String.valueOf(R.string.reward_unit));
        RewardedAdLoadCallback callback = new RewardedAdLoadCallback(){
            @Override
            public void onRewardedAdFailedToLoad(LoadAdError loadAdError) {
                super.onRewardedAdFailedToLoad(loadAdError);
            }

            @Override
            public void onRewardedAdLoaded() {
                super.onRewardedAdLoaded();
            }
        };
        this.rewardedAd.loadAd(new AdRequest.Builder().build(), callback);

        mAdView = findViewById(R.id.adViewProfileBottom);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this, R.style.DialogTheme);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message

        String LoginUsername = sessionManagement.getSession("FullName");
        String LoginUserMobile = sessionManagement.getSession("Mobile");
        LoginUserId = sessionManagement.getSession("id");

        FullName.setText(LoginUsername);
        Mobile.setText(LoginUserMobile);

        EditPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Password.setVisibility(View.VISIBLE);
                    ConfirmPassword.setVisibility(View.VISIBLE);
                } else {
                    Password.setVisibility(View.GONE);
                    PasswordError.setVisibility(View.GONE);

                    ConfirmPassword.setVisibility(View.GONE);
                    ConfirmPasswordError.setVisibility(View.GONE);
                }
            }
        });

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update.setEnabled(false);
                Boolean successBoolean = true;

                if (FullName.getText().toString().equals("")) {
                    successBoolean = false;
                    FullNameError.setVisibility(View.VISIBLE);
                }
                if (!FullName.getText().toString().equals("")) {
                    FullNameError.setVisibility(View.GONE);
                }

                if (Mobile.getText().toString().equals("")) {
                    successBoolean = false;
                    MobileError.setVisibility(View.VISIBLE);
                }
                if (!Mobile.getText().toString().equals("")) {
                    MobileError.setVisibility(View.GONE);
                }
                if (Mobile.getText().toString().length() < 10) {
                    successBoolean = false;
                    MobileError.setText("Please Enter Valid Mobile Number");
                    MobileError.setVisibility(View.VISIBLE);
                }

                if (EditPassword.isChecked()) {
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
                }

                if(successBoolean){
                    RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
                    String URL = "http://restrictionsolution.com/ci/trendz_world/user/Edit";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject responseData = new JSONObject(response);
                                Toast.makeText(getApplicationContext(), responseData.getString("message"), Toast.LENGTH_SHORT).show();
                                Update.setEnabled(true);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Update.setEnabled(true);
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
                            params.put("id", LoginUserId);
                            params.put("fullname", FullName.getText().toString());
                            params.put("mobile", Mobile.getText().toString());
                            params.put("password", Password.getText().toString());
                            params.put("passwordStatus", String.valueOf(EditPassword.isChecked()));
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                } else {
                    Update.setEnabled(true);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                SessionManagement sessionManagement = new SessionManagement(this);
                sessionManagement.ClearSession();
                finish();
                if (this.rewardedAd.isLoaded()) {
                    RewardedAdCallback callback = new RewardedAdCallback() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            Log.i("Show", "ads");
                        }

                        @Override
                        public void onRewardedAdOpened() {
                            super.onRewardedAdOpened();
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            super.onRewardedAdClosed();
                        }

                        @Override
                        public void onRewardedAdFailedToShow(AdError adError) {
                            super.onRewardedAdFailedToShow(adError);
                        }
                    };

                    this.rewardedAd.show(this, callback);
                }
                startActivity(new Intent(ProfileActivity.this, NavigationActivity.class));
                break;
            case R.id.menu_profiles:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}