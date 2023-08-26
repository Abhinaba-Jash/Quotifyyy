package com.example.qoute;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyVolleySingleton {
    private static MyVolleySingleton instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private MyVolleySingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized MyVolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new MyVolleySingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // Use ApplicationContext to prevent memory leaks
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}


