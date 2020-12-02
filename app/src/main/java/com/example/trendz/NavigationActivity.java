package com.example.trendz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.paykun.sdk.PaykunApiCall;
import com.paykun.sdk.eventbus.Events;
import com.paykun.sdk.eventbus.GlobalBus;
import com.paykun.sdk.helper.PaykunHelper;

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

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NavigationActivity extends AppCompatActivity {

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
        JSONObject object = new JSONObject();
        try {
            object.put("merchant_id", "984819566627936");
            object.put("access_token", "46F3E535AD12515A39A99C5753EC084F");
            object.put("customer_name",LoginUsername);
            object.put("customer_email",LoginUserEmail);
            object.put("customer_phone",LoginUserMobile);
            object.put("product_name", "Join Fee");
            object.put("order_no",System.currentTimeMillis()); // order no. should have 10 to 30 character in numeric format
            object.put("amount","2000");  // minimum amount should be 10
            object.put("isLive",false); // need to send false if you are in sandbox mode
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new PaykunApiCall.Builder(NavigationActivity.this).sendJsonObject(object); // Paykun api to initialize your payment and send info.
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getResults(Events.PaymentMessage message) {
        if(message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_SUCCESS)){
            // do your stuff here
            // message.getTransactionId() will return your failed or succeed transaction id
            /* if you want to get your transaction detail call message.getTransactionDetail()
             *  getTransactionDetail return all the field from server and you can use it here as per your need
             *  For Example you want to get Order id from detail use message.getTransactionDetail().order.orderId */
            if(!TextUtils.isEmpty(message.getTransactionId())) {
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
        }
        else if(message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_FAILED)){
            // do your stuff here
            Toast.makeText(NavigationActivity.this,"Your Transaction is failed",Toast.LENGTH_SHORT).show();
        }
        else if(message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_SERVER_ISSUE)){
            // do your stuff here
            Toast.makeText(NavigationActivity.this,PaykunHelper.MESSAGE_SERVER_ISSUE,Toast.LENGTH_SHORT).show();
        }else if(message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_ACCESS_TOKEN_MISSING)){
            // do your stuff here
            Toast.makeText(NavigationActivity.this,"Access Token missing",Toast.LENGTH_SHORT).show();
        }
        else if(message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_MERCHANT_ID_MISSING)){
            // do your stuff here
            Toast.makeText(NavigationActivity.this,"Merchant Id is missing",Toast.LENGTH_SHORT).show();
        }
        else if(message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_INVALID_REQUEST)){
            Toast.makeText(NavigationActivity.this,"Invalid Request",Toast.LENGTH_SHORT).show();
        }
        else if(message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_NETWORK_NOT_AVAILABLE)){
            Toast.makeText(NavigationActivity.this,"Network is not available",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Register this activity to listen to event.
        GlobalBus.getBus().register(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        // Unregister from activity
        GlobalBus.getBus().unregister(this);
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