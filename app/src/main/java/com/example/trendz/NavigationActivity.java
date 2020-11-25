package com.example.trendz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NavigationActivity extends AppCompatActivity implements PaymentResultListener {

    private AppBarConfiguration mAppBarConfiguration;
    String LoginUserId, LoginUsername, LoginUserEmail, LoginUserMobile;
    ProgressDialog progressDialog;
    FloatingActionButton fab;
    private long BackPressedTime;
    private Toast BackToast;
    private RewardedAd rewardedAd;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);

        Checkout.preload(getApplicationContext());

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

        progressDialog = new ProgressDialog(NavigationActivity.this, R.style.DialogTheme);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message


        SessionManagement sessionManagement = new SessionManagement(NavigationActivity.this);
        LoginUserId = sessionManagement.getSession("id");
        LoginUsername = sessionManagement.getSession("FullName");
        LoginUserEmail = sessionManagement.getSessionEmail();
        LoginUserMobile = sessionManagement.getSession("Mobile");

        if (LoginUserId.equals("1")) {
            navigationView.getMenu().findItem(R.id.nav_slideshow).setVisible(true);
        } else {
            navigationView.getMenu().findItem(R.id.nav_slideshow).setVisible(false);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        /*navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                Log.e("Menu", String.valueOf(destination.getId()));
            }
        });*/

        View MenuHeader =  navigationView.getHeaderView(0);
        TextView menu_user = (TextView)MenuHeader.findViewById(R.id.MenuHeaderLoginUserName);
        TextView menu_user_email = (TextView)MenuHeader.findViewById(R.id.MenuHeaderLoginUserEmail);

        if (sessionManagement.getSession("isJoin").equals("0")) {
            fab.setVisibility(View.VISIBLE);
        }

        menu_user.setText(LoginUsername);
        menu_user_email.setText(LoginUserEmail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
                startActivity(new Intent(NavigationActivity.this, MainActivity.class));
                break;
            case R.id.menu_profiles:
                startActivity(new Intent(NavigationActivity.this, ProfileActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void startPayment() {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.red_logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", LoginUsername);
//            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#FF5400");
            options.put("currency", "INR");
            options.put("amount", "200000");//pass amount in currency subunits
            options.put("prefill.email", LoginUserEmail);
            options.put("prefill.contact",LoginUserMobile);
            options.put("prefill.method", "card");
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        fab.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), "Your payment has been received successfully", Toast.LENGTH_SHORT).show();
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(NavigationActivity.this);
        String URL = "http://restrictionsolution.com/ci/trendz_world/user/join";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {
                        JSONObject res = new JSONObject(response);
                        Toast.makeText(getApplicationContext(), res.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                }
            }
        ) {
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
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(), "Payment Failed" + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar.getTitle().toString().equals("Home")) {
            if (BackPressedTime + 2000 > System.currentTimeMillis()) {
                BackToast.cancel();
                finish();
                super.onBackPressed();
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
                return;
            } else {
                BackToast = Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT);
                BackToast.show();
            }
            BackPressedTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }
}