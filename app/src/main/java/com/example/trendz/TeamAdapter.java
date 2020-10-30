package com.example.trendz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

    private List<TeamEntry> data;
    public TeamAdapter(List<TeamEntry> data){
        this.data = data;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_team_list, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        holder.number.setText(data.get(position).getNumber());
        holder.name.setText(data.get(position).getName());
        holder.sponsor.setText(data.get(position).getSponsor());
        holder.amount.setText(data.get(position).getAmount());
        holder.phone.setText(data.get(position).getPhone());
        holder.date.setText(data.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder {
        TextView number, name, sponsor, amount, phone, date;
        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.teamlistnumbertextView);
            name = itemView.findViewById(R.id.teamlistnametextView);
            sponsor = itemView.findViewById(R.id.teamlistsponsortextView);
            amount = itemView.findViewById(R.id.teamlistamounttextView);
            phone = itemView.findViewById(R.id.teamlistphonetextView);
            date = itemView.findViewById(R.id.teamlistdatetextView);
        }
    }
}
