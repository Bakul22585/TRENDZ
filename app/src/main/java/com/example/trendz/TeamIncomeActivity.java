package com.example.trendz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;

import java.util.ArrayList;
import java.util.List;

public class TeamIncomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<TeamEntry> TeamEntry = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_income);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Team Income List</font>"));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        recyclerView = findViewById(R.id.TeamincomeRV);
        TeamEntry teamEntry = new TeamEntry();
        teamEntry.setNumber("1");
        teamEntry.setName("Akash");
        teamEntry.setSponsor("Tarsariya Bakul Maganbhai");
        teamEntry.setAmount("50.00");
        teamEntry.setPhone("7202818684");
        teamEntry.setDate("26-10-2020");
        TeamEntry.add(teamEntry);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TeamAdapter(TeamEntry));
    }
}