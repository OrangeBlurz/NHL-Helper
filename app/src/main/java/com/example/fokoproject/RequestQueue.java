package com.example.fokoproject;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

/**
 * Singleton class responsible for making all the Volley requests
 * https://developer.android.com/training/volley/requestqueue
 */

public class RequestQueue {

    private static RequestQueue mInstance;
    private com.android.volley.RequestQueue mRequestQueue;
    private static Context mContext;

    private RequestQueue(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public com.android.volley.RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Singleton instance retrieval
     * @param context
     * @return RequestQueue instance
     */
    public static synchronized RequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestQueue(context);
        }
        return mInstance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
