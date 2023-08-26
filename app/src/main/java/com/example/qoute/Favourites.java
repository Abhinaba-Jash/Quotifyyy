package com.example.qoute;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Favourites extends AppCompatActivity {
RecyclerView recyclerView;
List<ModelData> data;
FirebaseAuth firebaseAuth;
FirebaseUser firebaseUser;
DatabaseReference databaseReference;
FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        String uid=firebaseUser.getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users").child(uid).child("Favourite");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data=new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    String quote= Objects.requireNonNull(dataSnapshot.child("quoteText").getValue()).toString();
                    String author= Objects.requireNonNull(dataSnapshot.child("quoteAuthor").getValue()).toString();
                    String timestamp= Objects.requireNonNull(dataSnapshot.child("cId").getValue()).toString();
                    ModelData newData=new ModelData(quote,author,timestamp);
                    data.add(newData);

                }
              adapter ad=new adapter(getContext(),data);
                recyclerView.setAdapter(ad);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Favourites.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });









    }
}