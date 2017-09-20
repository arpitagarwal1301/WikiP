package com.agarwal.arpit.wikip;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agarwal.arpit.wikip.dataclasses.Page;

import java.util.List;

/**
 * Created by arpitagarwal on 19/09/17.
 * Recycler adapter class for populating the list row items
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<Page> mDataList;

    public RecyclerAdapter(List<Page> dataList) {
        mDataList = dataList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Page data = mDataList.get(position);
        holder.title.setText(data.getTitle());
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        public MyViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
        }
    }

}
