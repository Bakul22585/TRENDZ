package com.example.trendz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.AbsListView;
import android.widget.ProgressBar;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AutoPoolIncomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager manager;
    AutopoolAdapter adapter;
    List<Object> AutopoolEntry = new ArrayList<>();
    Boolean IsScrolled = true;
    int CurrentItems, TotalItem, ScrollOutItem, PageIndex = 0;
    ProgressBar progressBar;
    String LoginUserId;
    public static final int ITEM_PER_AD = 4;
    private AdView mAdView, mAdView2;
    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_pool_income);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Auto Pool Income</font>"));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

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

        mAdView = findViewById(R.id.adViewAutoPool1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView2 = findViewById(R.id.adViewAutoPool2);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest2);

        recyclerView = findViewById(R.id.AutopoolRV);
        manager = new LinearLayoutManager(AutoPoolIncomeActivity.this);
        SessionManagement sessionManagement = new SessionManagement(AutoPoolIncomeActivity.this);
        LoginUserId = sessionManagement.getSession("id");

        final ProgressDialog progressDialog = new ProgressDialog(AutoPoolIncomeActivity.this, R.style.DialogTheme);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message

        RequestQueue requestQueue = Volley.newRequestQueue(AutoPoolIncomeActivity.this);
        String URL = "http://restrictionsolution.com/ci/trendz_world/user/getautopoolincomeentry?id=" + LoginUserId + "&index=" + PageIndex;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject res = new JSONObject(response);
                            JSONArray data = res.getJSONArray("data");

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject UserData = data.getJSONObject(i);

                                AutopoolEntry autopoolEntry = new AutopoolEntry();
                                autopoolEntry.setNumber(UserData.getString("index"));
                                autopoolEntry.setLevel(UserData.getString("level"));
                                autopoolEntry.setAmount(UserData.getString("amount"));
                                autopoolEntry.setDate(UserData.getString("created"));
                                AutopoolEntry.add(autopoolEntry);
                            }
                            adapter = new AutopoolAdapter(AutopoolEntry, AutoPoolIncomeActivity.this);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(manager);
                            getBannerAds();
                            loadBannerAds();

                            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL && IsScrolled) {
                                        IsScrolled = true;
                                    }
                                }

                                @Override
                                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);
                                    if (dy > 0) {
                                        CurrentItems = manager.getChildCount();
                                        TotalItem = manager.getItemCount();
                                        ScrollOutItem = manager.findFirstVisibleItemPosition();

                                        int Total = CurrentItems + ScrollOutItem;
                                        if (IsScrolled && (Total == TotalItem)) {
//                                        progressBar.setVisibility(View.VISIBLE);
                                            PageIndex++;
                                            IsScrolled = false;
                                            fetchData();
                                        }
                                    }
                                }
                            });
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
        );

        requestQueue.add(stringRequest);
    }

    private void fetchData() {
        RequestQueue requestQueue = Volley.newRequestQueue(AutoPoolIncomeActivity.this);
        String URL = "http://restrictionsolution.com/ci/trendz_world/user/getautopoolincomeentry?id=" + LoginUserId + "&index=" + PageIndex;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            JSONArray data = res.getJSONArray("data");

                            if (data.length() == 0) {
                                IsScrolled = false;
                            }

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject UserData = data.getJSONObject(i);

                                AutopoolEntry autopoolEntry = new AutopoolEntry();
                                autopoolEntry.setNumber(UserData.getString("index"));
                                autopoolEntry.setLevel(UserData.getString("level"));
                                autopoolEntry.setAmount(UserData.getString("amount"));
                                autopoolEntry.setDate(UserData.getString("created"));
                                AutopoolEntry.add(autopoolEntry);
                            }
                            adapter.notifyDataSetChanged();
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

    private void getBannerAds() {
        for (int i = 0; i < AutopoolEntry.size(); i+=ITEM_PER_AD ) {
            final AdView adView = new AdView(AutoPoolIncomeActivity.this);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(String.valueOf(R.string.banner_unit));
            AutopoolEntry.add(i, adView);
        }
    }

    private void loadBannerAds() {
        for (int i = 0; i < AutopoolEntry.size(); i++) {
            Object item = AutopoolEntry.get(i);

            if (item instanceof AdView) {
                final AdView adView = (AdView) item;
                adView.loadAd(new AdRequest.Builder().build());
            }
        }
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
                startActivity(new Intent(AutoPoolIncomeActivity.this, MainActivity.class));
                break;
            case R.id.menu_profiles:
                startActivity(new Intent(AutoPoolIncomeActivity.this, ProfileActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}