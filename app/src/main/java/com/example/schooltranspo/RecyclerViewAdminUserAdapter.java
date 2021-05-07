package com.example.schooltranspo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdminUserAdapter extends RecyclerView.Adapter<RecyclerViewAdminUserAdapter.ViewHolder> {

    private static final String Tag = "RecyclerView";
    private Context mContext;
    private ArrayList<UserData> userList;

    public RecyclerViewAdminUserAdapter(Context mContext, ArrayList<UserData> userList) {
        this.mContext = mContext;
        this.userList = userList;
    }

    @NonNull
    @Override
    public RecyclerViewAdminUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_user_item, parent, false);

        return new RecyclerViewAdminUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdminUserAdapter.ViewHolder holder, int position) {

//        String vehicleID = vehicleList.get(position).getVehicleID();
//        String operatorID = vehicleList.get(position).getOperatorID();

        holder.fullName.setText(userList.get(position).getFullName());
        holder.contact.setText(userList.get(position).getContact());
        holder.address.setText(userList.get(position).getAddress());
        holder.userType.setText(userList.get(position).getUserType());
    }

    @Override
    public int getItemCount() {

        return userList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        TextView fullName,  contact, address, userType;
        ImageView vehicleImage;
        //RelativeLayout parentLayout;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fullName = itemView.findViewById(R.id.fullName);
            contact = itemView.findViewById(R.id.contact);
            address = itemView.findViewById(R.id.address);
            userType = itemView.findViewById(R.id.userType);
            //parentLayout = itemView.findViewById(R.id.parentLayout);
            cardView = itemView.findViewById(R.id.useListCardView);
        }
    }
}
