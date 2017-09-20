package com.agarwal.arpit.wikip.networkclasses;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by arpitagarwal on 19/09/17.
 * Singleton class
 * Using volley library for asynchronously loading of data
 */

public class VolleyNetworkRequest {

    private static VolleyNetworkRequest networkInstance = new VolleyNetworkRequest();
    private RequestQueue mRequestQueue;

    public static VolleyNetworkRequest getInstance() {
        return networkInstance;
    }

    public RequestQueue getRequestQueue(Context context) {
        return mRequestQueue ==  null ? mRequestQueue = Volley.newRequestQueue(context) : mRequestQueue;
    }

    /**
     * Add the request to queue while assigning different tags to different requests
     * @param request
     * @param tag
     */
    public void addToRequestQueue(Context context,Request request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? context.getClass().getSimpleName() : tag);
        getRequestQueue(context).add(request);
    }

    public void cancelPendingRequest(Context context,String tag) {
        VolleyNetworkRequest.getInstance().getRequestQueue(context).cancelAll(tag);
    }


}