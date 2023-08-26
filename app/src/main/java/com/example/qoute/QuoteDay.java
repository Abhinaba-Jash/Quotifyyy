package com.example.qoute;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QuoteDay extends AppCompatActivity {
    TextView quote,author;
    String quo=null;
    String auth=null;
    ImageView share;
    ProgressBar progressBar;
    final String[] returnText = new String[1];
    // Replace "your_api_key_here" with your actual API key
    String apiKey = "CjyqMHdit2fGU2cHmPGMxg==h5OnfBdWw96j4ArT";
    // URL of the API
    String apiUrl = "https://api.api-ninjas.com/v1/quotes?category=happiness";
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_day);
        quote=findViewById(R.id.quoteoftheday);
        author=findViewById(R.id.authoroftheday);
        share=findViewById(R.id.share);
        progressBar=findViewById(R.id.progressBar3);
        loadApi();
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnText[0]=quo+" ~ "+auth;
                shareText(returnText[0]);
            }
        });

    }

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



    private String loadApi() {

        //progressBar.setVisibility(View.VISIBLE);
        // Initialize the Singleton
        progressBar.setVisibility(View.VISIBLE);
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
                        //progressBar.setVisibility(View.INVISIBLE);
                        JSONObject jsonObject=null;
                        try {
                            jsonObject =response.getJSONObject(0);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        try {
                            quo =jsonObject.getString("quote");

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        try {
                            auth=jsonObject.getString("author");
                            //author.setText("~ " +auth);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        returnText[0] =quo+auth;
                        quote.setText(quo);
                        author.setText(" ~ "+auth);
                        progressBar.setVisibility(View.INVISIBLE);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(QuoteDay.this, error.toString(), Toast.LENGTH_SHORT).show();
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
        return returnText[0];
    }
}
