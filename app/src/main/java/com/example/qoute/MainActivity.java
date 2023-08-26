package com.example.qoute;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
FirebaseAuth firebaseAuth;
FirebaseUser firebaseUser;
FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;

    TextView dayquote,quote,author,next;
    LinearLayout favouritelayout,sharelayout;
    ImageView love,logout;
    ProgressBar progressBar;

    final String[] returnText = new String[1];
    String quo=null;
    String auth=null;

    // Replace "your_api_key_here" with your actual API key
    String apiKey = "CjyqMHdit2fGU2cHmPGMxg==h5OnfBdWw96j4ArT";

    // URL of the API
    String apiUrl = "https://api.api-ninjas.com/v1/quotes?category=happiness";
String shareTxt;
boolean tap=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent=getIntent();

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");
dayquote=findViewById(R.id.dayquote);
quote=findViewById(R.id.quote);
author=findViewById(R.id.author);
next=findViewById(R.id.next);
love=findViewById(R.id.love);
logout=findViewById(R.id.logout);
sharelayout=findViewById(R.id.sharelayout);
favouritelayout=findViewById(R.id.favouritelayout);
progressBar=findViewById(R.id.progressBar);
loadApi();




next.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
       love.setImageResource(R.drawable.favourite);
        tap=false;
        loadApi();
    }
});
sharelayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        shareText(returnText[0]);
    }
});
dayquote.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(MainActivity.this,QuoteDay.class);
        startActivity(intent);

    }
});
love.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(!tap) {
            love.setImageResource(R.drawable.favouritefill);
            addToFavourite();
            tap=true;
        }
    }
});

favouritelayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(MainActivity.this,Favourites.class);
        startActivity(intent);
    }
});
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view = LayoutInflater.from(view.getContext()).inflate(R.layout.logoutdialog, null);
                Button logout = view.findViewById(R.id.logout);
                Button cancel=view.findViewById(R.id.no);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();
                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(MainActivity.this,login.class);
                        FirebaseAuth.getInstance().signOut();
                        MainActivity.this.finish();
                        startActivity(intent);


                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                    }
                });

            }
        });
    }

    private void addToFavourite() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String userId=firebaseUser.getUid();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Favourite");
        HashMap<String, Object> data=new HashMap<>();
        data.put("cId",timestamp);
        data.put("quoteText",quo);
        data.put("quoteAuthor",auth);
        databaseReference.child(timestamp).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MainActivity.this, "Added to your favourite....", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Some error occurred,try again.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @SuppressLint("QueryPermissionsNeeded")
        private void shareText(String textToShare) {
            // Create an Intent with the ACTION_SEND action
            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            // Set the MIME type to plain text
            shareIntent.setType("text/plain");

            // Add the text to share
            shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);

            // Create a chooser dialog to let the user choose how to share
            Intent chooser = Intent.createChooser(shareIntent, "Share via");

            // Check if there are apps that can handle this intent
            if (shareIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(chooser);
            } else {
                // Handle the case where no suitable apps are installed
                Toast.makeText(this, "No apps can handle this request.", Toast.LENGTH_SHORT).show();
            }
        }


    private void loadApi() {

        progressBar.setVisibility(View.VISIBLE);
        // Initialize the Singleton
        MyVolleySingleton volleySingleton = MyVolleySingleton.getInstance(this);
        // Create a JsonObjectRequest with the x-api-key header
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONArray response) {
progressBar.setVisibility(View.INVISIBLE);
                        JSONObject jsonObject=null;
                        try {
                            jsonObject =response.getJSONObject(0);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        try {
                            quo =jsonObject.getString("quote");

                            quote.setText("\"" +quo+" \"");

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        try {
                            auth=jsonObject.getString("author");

                            author.setText("~ " +auth);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        returnText[0] =quo+" ~ "+auth;

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }


        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-api-key", apiKey);
                return headers;
            }
        };

// Add the request to the RequestQueue

        volleySingleton.addToRequestQueue(jsonObjectRequest);


    }
}