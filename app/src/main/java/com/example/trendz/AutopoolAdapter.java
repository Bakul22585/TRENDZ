package com.example.trendz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AutopoolAdapter extends RecyclerView.Adapter<AutopoolAdapter.AutopoolViewHolder> {

    private List<AutopoolEntry> data;
    public AutopoolAdapter(List<AutopoolEntry> data){
        this.data = data;
    }

    @NonNull
    @Override
    public AutopoolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_autopool_list, parent, false);
        return new AutopoolViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AutopoolViewHolder holder, int position) {
        holder.number.setText(data.get(position).getNumber());
        holder.level.setText(data.get(position).getLevel());
        holder.amount.setText(data.get(position).getAmount());
        holder.date.setText(data.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class AutopoolViewHolder extends RecyclerView.ViewHolder {
        TextView number, level, amount, date;
        public AutopoolViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.autopoollistnumberTv);
            level = itemView.findViewById(R.id.autopoollistlevelTV);
            amount = itemView.findViewById(R.id.autopoollistamountTV);
            date = itemView.findViewById(R.id.autopoollistdateTV);
        }
    }
}
