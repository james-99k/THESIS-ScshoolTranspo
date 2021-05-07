package com.example.schooltranspo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewOperator2Adapter extends RecyclerView.Adapter<RecyclerViewOperator2Adapter.ViewHolder>{
    private static final String Tag = "RecyclerView";
    private Context mContext;
    private ArrayList<Subscriber> vehicleList;

    public RecyclerViewOperator2Adapter(Context mContext, ArrayList<Subscriber> vehicleList) {
        this.mContext = mContext;
        this.vehicleList = vehicleList;
    }

    @NonNull
    @Override
    public RecyclerViewOperator2Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_operator2, parent, false);

        return new RecyclerViewOperator2Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewOperator2Adapter.ViewHolder holder, int position) {
//        String vehicle = vehicleList.get(position).getVehicles();
//        String driver = vehicleList.get(position).getDriverName();

        String vehicleID = vehicleList.get(position).getVehicleID();
//        String operatorID = vehicleList.get(position).getOperatorID();

        String passID = vehicleList.get(position).getPassengerID();

        holder.passName.setText(vehicleList.get(position).getPassengerName());
        holder.vehicleName.setText(vehicleList.get(position).getVehicles());
        holder.status.setText(vehicleList.get(position).getStatus());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewPassenger.class);
                intent.putExtra("passID", passID);
                intent.putExtra("vehicleID", vehicleID);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder{

        TextView passName, vehicleName, status;
        ImageView vehicleImage;
        //RelativeLayout parentLayout;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            passName = itemView.findViewById(R.id.passName);
            vehicleName = itemView.findViewById(R.id.vehicleName);
            vehicleImage = itemView.findViewById(R.id.vehicleImage);
            status = itemView.findViewById(R.id.status);
            cardView = itemView.findViewById(R.id.vehicleListCardView);
        }
    }
}
