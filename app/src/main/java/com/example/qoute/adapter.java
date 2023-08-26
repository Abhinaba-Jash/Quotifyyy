package com.example.qoute;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.AccessControlContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class adapter extends RecyclerView.Adapter<adapter.MyHolder> {

    List<ModelData> data;
    AccessControlContext context;


    public adapter(AccessControlContext context, List<ModelData> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adapter,parent,false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        long dateInMillis=Long.parseLong(data.get(position).getTimestamp());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy   hh:mm aa");
        String dateString = formatter.format(new Date(dateInMillis));
        holder.time.setText(dateString);
        String txt=data.get(position).getQuote()+" ~ "+data.get(position).getAuthor();
        holder.quote.setText(txt);
        String cId=data.get(position).getTimestamp();
        String uid= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Favourite").child(cId);
                databaseReference.removeValue();

            }
        });



    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView quote,author,time;
        ImageView delete;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            quote=itemView.findViewById(R.id.quotetext);
            time=itemView.findViewById(R.id.timestamp);
delete=itemView.findViewById(R.id.delete);


        }
    }
}


