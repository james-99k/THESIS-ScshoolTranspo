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

public class RecyclerViewListOfStudents extends RecyclerView.Adapter<RecyclerViewListOfStudents.ViewHolder> {

    private static final String Tag = "RecyclerView";
    private Context mContext;
    private ArrayList<UserData> userList;

    public RecyclerViewListOfStudents(Context mContext, ArrayList<UserData> userList) {
        this.mContext = mContext;
        this.userList = userList;
    }

    @NonNull
    @Override
    public RecyclerViewListOfStudents.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_passenger, parent, false);

        return new RecyclerViewListOfStudents.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewListOfStudents.ViewHolder holder, int position) {

        String passID = userList.get(position).getPassengerID();

        holder.passName.setText(userList.get(position).getFullName());
        holder.address.setText(userList.get(position).getAddress());
        holder.schoolName.setText(userList.get(position).getSchoolName());
        holder.contact.setText(userList.get(position).getContact());


//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, DriverViewPassengerProfile.class);
//                intent.putExtra("PassengerID", passID);
//                mContext.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView passName, address, schoolName, contact;
        ImageView vehicleImage;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            passName = itemView.findViewById(R.id.passName);
            address = itemView.findViewById(R.id.address);
            schoolName = itemView.findViewById(R.id.schoolName);
            contact = itemView.findViewById(R.id.contact);
            cardView = itemView.findViewById(R.id.passengerListCardView);
        }
    }
}
