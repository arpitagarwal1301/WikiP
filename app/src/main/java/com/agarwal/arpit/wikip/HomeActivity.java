package com.agarwal.arpit.wikip;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.agarwal.arpit.wikip.dataclasses.Page;
import com.agarwal.arpit.wikip.dataclasses.ParsedResponse;
import com.agarwal.arpit.wikip.networkclasses.GsonVolleyRequest;
import com.agarwal.arpit.wikip.networkclasses.VolleyNetworkRequest;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.agarwal.arpit.wikip.ConstantsData.JSON_WIKI_URL;
import static com.agarwal.arpit.wikip.ConstantsData.JSON_WIKI_URL_TAG;

public class HomeActivity extends AppCompatActivity{


    @BindView(R.id.swipe_refresh_view)
    MultiSwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.content_recycler_view)
    RecyclerView recyclerView;

    private RecyclerAdapter recyclerAdapter;
    private List<Page> pageList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setUpController();
        sendDataRequest();
    }

    private void setUpController() {

        recyclerAdapter = new RecyclerAdapter(pageList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);

        swipeRefreshLayout.setSwipeableChildren(R.id.content_recycler_view);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendDataRequest();
            }
        });
    }

    /**
     * Sending request using Gson Volley Request and receiveing the parsed response
     */
    private void sendDataRequest() {

        GsonVolleyRequest gsonRequest = new GsonVolleyRequest(JSON_WIKI_URL, ParsedResponse.class, null, new Response.Listener<ParsedResponse>() {
            @Override
            public void onResponse(ParsedResponse response) {
                // Handle response
                handleResponse(response);
                stopRefreshing();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Handle error
                Log.i("Error Response",error.toString());
                Toast.makeText(HomeActivity.this,"Error Received",Toast.LENGTH_SHORT).show();
            }
        });
        gsonRequest.makeReqeust(this,gsonRequest, JSON_WIKI_URL_TAG);

    }

    /**
     * handle and opulate the received data
     * @param response
     */
    public void handleResponse(ParsedResponse response) {

        Log.i("Response,homeActivity",response.toString());

        //Populating titles in the list
        pageList.clear();
        pageList.addAll(response.getQuery().getPages());
        recyclerAdapter.notifyDataSetChanged();

    }


    private void stopRefreshing(){
        if (swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleyNetworkRequest.getInstance().cancelPendingRequest(this,JSON_WIKI_URL_TAG);
    }
}
