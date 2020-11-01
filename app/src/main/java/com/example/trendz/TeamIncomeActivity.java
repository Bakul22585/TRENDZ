package com.example.trendz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ProgressBar;

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

import java.util.ArrayList;
import java.util.List;

public class TeamIncomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager manager;
    TeamAdapter adapter;
    List<Object> TeamEntry = new ArrayList<>();
    Boolean IsScrolled = true;
    int CurrentItems, TotalItem, ScrollOutItem, PageIndex = 0;
    ProgressBar progressBar;
    String LoginUserId;
    public static final int ITEM_PER_AD = 4;
    private AdView mAdView, mAdView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_income);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Team Income</font>"));
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

        mAdView = findViewById(R.id.adViewTeamIncome1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView2 = findViewById(R.id.adViewTeamIncome2);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest2);

        recyclerView = findViewById(R.id.TeamincomeRV);
        manager = new LinearLayoutManager(TeamIncomeActivity.this);
        SessionManagement sessionManagement = new SessionManagement(TeamIncomeActivity.this);
        LoginUserId = sessionManagement.getSession("id");

        final ProgressDialog progressDialog = new ProgressDialog(TeamIncomeActivity.this, R.style.DialogTheme);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message

        getBannerAds();

        RequestQueue requestQueue = Volley.newRequestQueue(TeamIncomeActivity.this);
        String URL = "http://restrictionsolution.com/ci/trendz_world/user/getteamincomeentry?id=" + LoginUserId + "&index=" + PageIndex;

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

                            TeamEntry teamEntry = new TeamEntry();
                            teamEntry.setNumber(UserData.getString("index"));
                            teamEntry.setName(UserData.getString("fullname"));
                            teamEntry.setSponsor(UserData.getString("sponsorname"));
                            teamEntry.setAmount(UserData.getString("amount"));
                            teamEntry.setPhone(UserData.getString("mobile"));
                            teamEntry.setDate(UserData.getString("created"));
                            TeamEntry.add(teamEntry);
                        }
                        adapter = new TeamAdapter(TeamEntry, TeamIncomeActivity.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(manager);


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
        RequestQueue requestQueue = Volley.newRequestQueue(TeamIncomeActivity.this);
        String URL = "http://restrictionsolution.com/ci/trendz_world/user/getteamincomeentry?id=" + LoginUserId + "&index=" + PageIndex;

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

                            TeamEntry teamEntry = new TeamEntry();
                            teamEntry.setNumber(UserData.getString("index"));
                            teamEntry.setName(UserData.getString("fullname"));
                            teamEntry.setSponsor(UserData.getString("sponsorname"));
                            teamEntry.setAmount(UserData.getString("amount"));
                            teamEntry.setPhone(UserData.getString("mobile"));
                            teamEntry.setDate(UserData.getString("created"));
                            TeamEntry.add(teamEntry);
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
        for (int i = 0; i < TeamEntry.size(); i+=ITEM_PER_AD ) {
            final AdView adView = new AdView(TeamIncomeActivity.this);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(String.valueOf(R.string.banner_unit));
            TeamEntry.add(i, adView);
        }
        loadBannerAds();
    }

    private void loadBannerAds() {
        for (int i = 0; i < TeamEntry.size(); i++) {
            Object item = TeamEntry.get(i);

            if (item instanceof AdView) {
                final AdView adView = (AdView) item;
                adView.loadAd(new AdRequest.Builder().build());
            }
        }
    }
}