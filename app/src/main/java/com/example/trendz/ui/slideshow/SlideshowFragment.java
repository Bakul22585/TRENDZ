package com.example.trendz.ui.slideshow;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.trendz.AutoPoolIncomeActivity;
import com.example.trendz.AutopoolAdapter;
import com.example.trendz.AutopoolEntry;
import com.example.trendz.ProfileActivity;
import com.example.trendz.R;
import com.example.trendz.SessionManagement;
import com.example.trendz.User;
import com.example.trendz.WithdrawRequestAdapter;
import com.example.trendz.WithdrawRequestEntry;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    WithdrawRequestAdapter adapter;
    List<Object> WithdrawRequestEntry = new ArrayList<>();
    Boolean IsScrolled = true;
    int CurrentItems, TotalItem, ScrollOutItem, PageIndex = 0;
    String LoginUserId, SelectedOption = "0", Field = "created", Order = "ASC";
    private AdView mAdView, mAdView2;
    Spinner WithdrawRequestStatusOption;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        recyclerView = root.findViewById(R.id.WithdrawRequestRV);
        WithdrawRequestStatusOption = root.findViewById(R.id.selectWithdrawRequestStatus);
        swipeRefreshLayout = root.findViewById(R.id.withdrawRequestListSwipeRefresh);
        manager = new LinearLayoutManager(getActivity());
        SessionManagement sessionManagement = new SessionManagement(getActivity());
        LoginUserId = sessionManagement.getSession("id");

        AdView adView = new AdView(getActivity());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(String.valueOf(R.string.banner_unit));

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = root.findViewById(R.id.adViewWithdrawRequest1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView2 = root.findViewById(R.id.adViewWithdrawRequest2);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest2);

        List<String> option = new ArrayList<>();
        option.add("Pending");    option.add("Completed");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, option);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        WithdrawRequestStatusOption.setAdapter(dataAdapter);

        progressDialog = new ProgressDialog(getActivity(), R.style.DialogTheme);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            PageIndex = 0;
            WithdrawRequestEntry.clear();
            fetchData();
            swipeRefreshLayout.setRefreshing(false);
            }
        });

        WithdrawRequestStatusOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();

                if (value.equals("Pending")) {
                    SelectedOption = "0";
                    Order = "ASC";
                }

                if (value.equals("Completed")) {
                    SelectedOption = "1";
                    Order = "DESC";
                }
                PageIndex = 0;
                WithdrawRequestEntry.clear();
                fetchData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return root;
    }

    private void fetchData() {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = "http://restrictionsolution.com/ci/trendz_world/user/getwithdrawRequestlist?status=" + SelectedOption + "&index=" + PageIndex + "&field=" + Field + "&order=" + Order;

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

                                WithdrawRequestEntry withdrawRequestEntry = new WithdrawRequestEntry();
                                withdrawRequestEntry.setId(UserData.getString("id"));
                                withdrawRequestEntry.setUser_id(UserData.getString("user_id"));
                                withdrawRequestEntry.setNumber(UserData.getString("index"));
                                withdrawRequestEntry.setName(UserData.getString("fullname"));
                                withdrawRequestEntry.setAccount_number(UserData.getString("accountnumber"));
                                withdrawRequestEntry.setIfsc_code(UserData.getString("ifsccode"));
                                withdrawRequestEntry.setAmount(UserData.getString("amount"));
                                withdrawRequestEntry.setDate(UserData.getString("created"));
                                WithdrawRequestEntry.add(withdrawRequestEntry);
                            }
                            adapter = new WithdrawRequestAdapter(WithdrawRequestEntry, getActivity());
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(manager);

                            adapter.setListener(new WithdrawRequestAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    Log.e("Item Click", position + "");
                                }

                                @Override
                                public void onDeleteClick(int position) {
                                    Log.e("pay Click",  position + "");
//                                    fetchData();
                                    payAmount(position);
                                }
                            });

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
                                            ScrollData();
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

    private void ScrollData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = "http://restrictionsolution.com/ci/trendz_world/user/getwithdrawRequestlist?status=" + SelectedOption + "&index=" + PageIndex + "&field=" + Field + "&order=" + Order;

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

                                WithdrawRequestEntry withdrawRequestEntry = new WithdrawRequestEntry();
                                withdrawRequestEntry.setNumber(UserData.getString("index"));
                                withdrawRequestEntry.setName(UserData.getString("fullname"));
                                withdrawRequestEntry.setAccount_number(UserData.getString("accountnumber"));
                                withdrawRequestEntry.setIfsc_code(UserData.getString("ifsccode"));
                                withdrawRequestEntry.setAmount(UserData.getString("amount"));
                                withdrawRequestEntry.setDate(UserData.getString("created"));
                                WithdrawRequestEntry.add(withdrawRequestEntry);
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

    private void payAmount(final int position) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = "http://restrictionsolution.com/ci/trendz_world/user/payAmount";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject responseData = new JSONObject(response);
                    Toast.makeText(getActivity(), responseData.getString("message"), Toast.LENGTH_SHORT).show();
                    if (responseData.getBoolean("success")) {
                        WithdrawRequestEntry.remove(position);
                        adapter.notifyDataSetChanged();
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
                WithdrawRequestEntry withdrawRequestEntry = (WithdrawRequestEntry) WithdrawRequestEntry.get(position);
                params.put("id", withdrawRequestEntry.getId());
                params.put("user_id", withdrawRequestEntry.getUser_id());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}