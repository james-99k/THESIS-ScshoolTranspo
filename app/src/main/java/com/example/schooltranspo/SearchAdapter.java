package com.example.schooltranspo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.searchViewHolder>{

    String[] sampleData = {};

    private LayoutInflater layoutInflater;

    SearchAdapter(String[] _data){
        sampleData = _data;
    }
    @NonNull
    @Override
    public searchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.search_cardview, parent, false);

        return new searchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull searchViewHolder holder, int position) {
        String name = sampleData[position];
        holder.name.setText(name);
    }

    @Override
    public int getItemCount() {
        return sampleData.length;
    }

    public class searchViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name, dest, seat, rating, fee;
        public searchViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.ivSearchProfile);
            name = itemView.findViewById(R.id.searchProfileSchoolName);
            dest = itemView.findViewById(R.id.searchProfileSchoolDestination);
            seat = itemView.findViewById(R.id.searchProfileCapacity);
            rating = itemView.findViewById(R.id.searchProfileRating);
            fee = itemView.findViewById(R.id.searchProfileFee);
        }
    }
}
