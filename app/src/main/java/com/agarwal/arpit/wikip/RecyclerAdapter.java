package com.agarwal.arpit.wikip;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agarwal.arpit.wikip.dataclasses.Page;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by arpitagarwal on 19/09/17.
 * Recycler adapter class for populating the list row items
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> implements View.OnClickListener {

    private List<Page> mDataList;
    private Context mContext;
    private OnItemClickedAction mOnItemClickedAction;

    public RecyclerAdapter(Context context,List<Page> dataList,OnItemClickedAction onItemClickedAction) {
        mDataList = dataList;
        mContext = context;
        mOnItemClickedAction = onItemClickedAction;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Page data = mDataList.get(position);
        holder.title.setText(data.getTitle());
        StringBuilder builder = new StringBuilder();
        try {
            for (String str :data.getTerms().getDescription() ){
                builder.append(str + "\n");
            }
        }catch (Exception e){
            e.printStackTrace();
            builder.append("Desciption Not Found !");
        }
        holder.desc.setText(builder);

        if (null != data.getThumbnail()){

           final int imgDim = (int) mContext.getResources().getDimension(R.dimen.image_view_dimen);
            Picasso.with(mContext).load(data.getThumbnail().getSource()).resize(imgDim,imgDim).into(holder.img) ;
        }
        holder.itemLayout.setOnClickListener(this);
        holder.itemLayout.setTag(position);
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickedAction != null){
            mOnItemClickedAction.onClick(view);
        }
    }

    public interface OnItemClickedAction{
        void onClick(View view);
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout itemLayout;
        TextView title;
        TextView desc;
        ImageView img;
        public MyViewHolder(View itemView) {
            super(itemView);

            itemLayout = itemView.findViewById(R.id.item_layout);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.description);
            img = itemView.findViewById(R.id.image_view);
        }
    }

}
