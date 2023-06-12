package com.shahruie.zanankoochak.adater;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.shahruie.zanankoochak.R;
import com.shahruie.zanankoochak.app.AppController;
import com.shahruie.zanankoochak.model.Movie;

import java.util.List;


public class MyRecyclerViewAdapter  extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Movie> movieItems;
    private  Context context;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private double myNum;

    public MyRecyclerViewAdapter(Context context, List<Movie> movieItems) {
        this.mInflater = LayoutInflater.from(context);
        this.context=context;
        this.movieItems = movieItems;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        Movie m = movieItems.get(position);
        holder.thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
        holder.title.setText(m.getTitle());
        try {
             myNum = (Integer.parseInt(m.getTime()))/60;
        } catch(NumberFormatException nfe) {}
        holder.time.setText(myNum+"'");
        holder.visited.setText("بازدید: "+m.getVisited()+"");
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return movieItems.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        NetworkImageView thumbNail;
        TextView title,time,visited;
        ViewHolder(View itemView) {
            super(itemView);
             thumbNail = (NetworkImageView) itemView
                    .findViewById(R.id.thumbnail);
            title = (TextView) itemView.findViewById(R.id.title);
            visited = (TextView) itemView.findViewById(R.id.visited);
            time = (TextView) itemView.findViewById(R.id.time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    int getItem(int id) {
        return movieItems.size();
    }
    public void setItems(List<Movie> movies) {
        this.movieItems = movies;
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
