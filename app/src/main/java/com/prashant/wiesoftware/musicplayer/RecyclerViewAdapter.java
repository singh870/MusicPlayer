package com.prashant.wiesoftware.musicplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Wiesoftware on 4/19/2017.
 */

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.recyclerViewHolder> {

    private Context context;
    private List<HashMap<String, String>> list;
    private MediaMetadataRetriever metaRetriver = new MediaMetadataRetriever();

    RecyclerViewAdapter(Context context, List<HashMap<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public recyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
        return new recyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(recyclerViewHolder holder, int position) {

        HashMap<String, String> singleItem = list.get(position);
        holder.title.setText(singleItem.get("songTitle"));
        holder.subTitle.setText(singleItem.get("artistName")+" | "+singleItem.get("albumName"));

        metaRetriver.setDataSource(singleItem.get("pathId"));
        try {
            byte[] art = metaRetriver.getEmbeddedPicture();
            if (art != null) {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 2;
                Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length, opt);
                holder.albumImage.setImageBitmap(songImage);
            } else {
                holder.albumImage.setImageResource(R.drawable.music_player);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class recyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView albumImage;
        TextView title, subTitle;

        recyclerViewHolder(View itemView) {
            super(itemView);

            albumImage = (ImageView)itemView.findViewById(R.id.imageView);
            title = (TextView) itemView.findViewById(R.id.title);
            subTitle = (TextView)itemView.findViewById(R.id.description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Starting new intent
            Intent intent = new Intent(context,
                    AndroidBuildingMusicPlayerActivity.class);
            // Sending songIndex to PlayerActivity
            intent.putExtra("songIndex", getAdapterPosition());
            ((Activity)context).setResult(100, intent);
            // Closing PlayListView
            ((Activity)context).finish();
        }
    }

}
