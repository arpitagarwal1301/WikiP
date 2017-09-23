package com.agarwal.arpit.wikip;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.swipe_refresh_view)
    MultiSwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.content_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.search_bar_layout)
    FrameLayout searchBarLayout;
    @BindView(R.id.search_icon)
    ImageView searchBtn;
    @BindView(R.id.selected_value)
    EditText selectedValue;
    @BindView(R.id.default_header)
    RelativeLayout defaultHeaderLayout;
    @BindView(R.id.clear_search_txt)
    TextView clearSearchText;

    private ParsedResponse mParsedResponse;
    private RecyclerAdapter recyclerAdapter;
    private List<Page> pageList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setUpController();
        sendDataRequest();
        setUpViews();
    }

    private void setUpController() {

        recyclerAdapter = new RecyclerAdapter(this,pageList);

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
    @SuppressWarnings("unchecked")
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
                stopRefreshing();
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

        mParsedResponse = response;
        //Populating titles in the list
        pageList.clear();
        pageList.addAll(response.getQuery().getPages());
        recyclerAdapter.notifyDataSetChanged();
    }


    private void setUpViews() {

        clearSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(HomeActivity.this, android.R.anim.slide_out_right);
                selectedValue.startAnimation(anim);
                selectedValue.setVisibility(View.INVISIBLE);
                clearSearchText.setVisibility(View.GONE);
                searchBtn.setVisibility(View.VISIBLE);
                selectedValue.setText("");
                defaultHeaderLayout.setVisibility(View.VISIBLE);

                pageList.clear();
                pageList.addAll(mParsedResponse.getQuery().getPages());
                recyclerAdapter.notifyDataSetChanged();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideKeyBoard(HomeActivity.this, selectedValue);
                    }
                }, 400);
            }
        });

        selectedValue.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                    if (s != null && s.length() > 0) {
                        List<Page> filterList = new ArrayList<>();
                        for (int i = 0; i < pageList.size(); i++) {
                            if (pageList.get(i).getTitle().toLowerCase().contains(s.toString().toLowerCase())) {
                                filterList.add(pageList.get(i));
                            }
                        }
                        pageList.clear();
                        pageList.addAll(filterList);
                    } else {
                        pageList.clear();
                        pageList.addAll(mParsedResponse.getQuery().getPages());
                    }

                    recyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideOrShowEditText();
            }
        });
    }

    private void hideOrShowEditText() {
        if (selectedValue.getVisibility() != View.VISIBLE) {
            Animation anim = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
            selectedValue.startAnimation(anim);
            defaultHeaderLayout.setVisibility(View.GONE);
            searchBarLayout.setVisibility(View.VISIBLE);
            selectedValue.setVisibility(View.VISIBLE);
            clearSearchText.setVisibility(View.VISIBLE);
            searchBtn.setVisibility(View.GONE);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showKeyBoard(HomeActivity.this, selectedValue);
                }
            }, 400);
        } else {
        }
    }


    /**
     * To hide keyboard for EditText
     * @param context
     * @param editText
     */
    public void hideKeyBoard(final Context context, final EditText editText) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * To show keyboard for EditText
     * @param context
     * @param edit
     */
    public void showKeyBoard(Context context, EditText edit) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            edit.requestFocus();
            inputManager.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
        }
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
