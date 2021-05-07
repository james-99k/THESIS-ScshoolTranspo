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

public class RecyclerViewOperatorAdapter extends RecyclerView.Adapter<RecyclerViewOperatorAdapter.ViewHolder> {

    private static final String Tag = "RecyclerView";
    private Context mContext;
    private ArrayList<Operator> vehicleList;

    public RecyclerViewOperatorAdapter(Context mContext, ArrayList<Operator> vehicleList) {
        this.mContext = mContext;
        this.vehicleList = vehicleList;
    }

    @NonNull
    @Override
    public RecyclerViewOperatorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.operator_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String vehicle = vehicleList.get(position).getVehicles();
        String driver = vehicleList.get(position).getDriverName();

        String vehicleID = vehicleList.get(position).getVehicleID();
//        String operatorID = vehicleList.get(position).getOperatorID();

        holder.driverName.setText(vehicleList.get(position).getDriverName());

        holder.vehicleName.setText(vehicleList.get(position).getVehicles());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, vehicleProfile.class);
                intent.putExtra("vehicleBrand", vehicle);
                intent.putExtra("driverName", driver);
                intent.putExtra("vehicleID", vehicleID);
//                intent.putExtra("operatorID", operatorID);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return vehicleList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        TextView driverName, vehicleName;
        ImageView vehicleImage;
        //RelativeLayout parentLayout;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            driverName = itemView.findViewById(R.id.driverName);
            vehicleName = itemView.findViewById(R.id.VehicleName);
            vehicleImage = itemView.findViewById(R.id.vehicleImage);
            //parentLayout = itemView.findViewById(R.id.parentLayout);
            cardView = itemView.findViewById(R.id.vehicleListCardView);


        }
    }
}
