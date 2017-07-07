package com.example.mohammed.withoutname;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hitomi.cmlibrary.CircleMenu;

import java.util.List;

/**
 * Created by Mohammed on 18/05/16.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private CircleMenu circleMenu;
    private Context mContext;
    private List<Album> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public RelativeLayout RL;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            RL = (RelativeLayout) view.findViewById(R.id.RL);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }

    public AlbumsAdapter(Context mContext, List<Album> albumList, CircleMenu circleMenu) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.circleMenu = circleMenu;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Album album = albumList.get(position);
        holder.title.setText(album.getName());
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoCategory(album.getName());
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoCategory(album.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public void GoCategory(String CategoryName) {
        if (circleMenu.isOpened()) {
            circleMenu.closeMenu();
        } else {
            PublicParamaters.CategoryName = CategoryName;
            Intent intent = new Intent(mContext, SearchTagActivity.class);
            mContext.startActivity(intent);
        }
    }
}