package com.example.trendz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;

import java.util.ArrayList;
import java.util.List;

public class AutoPoolIncomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<AutopoolEntry> AutopoolEntry = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_pool_income);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Auto Pool Income List</font>"));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        recyclerView = findViewById(R.id.AutopoolRV);
        AutopoolEntry autopoolEntry = new AutopoolEntry();
        autopoolEntry.setNumber("1");
        autopoolEntry.setLevel("1");
        autopoolEntry.setAmount("50.00");
        autopoolEntry.setDate("26-10-2020");
        AutopoolEntry.add(autopoolEntry);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AutopoolAdapter(AutopoolEntry));
    }
}