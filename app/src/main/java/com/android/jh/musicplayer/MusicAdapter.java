package com.android.jh.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.Holder> {
    List<Music> datas;
    Context context;
    Intent intent = null;

    public MusicAdapter(Context context) {
        this.datas = DataLoader.get(context);
        this.context = context;
        this.intent = new Intent(context,PlayerActivity.class);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item,parent,false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Music music = datas.get(position);
        holder.txtTitle.setText(music.title);
        holder.txtArtist.setText(music.artist);

        holder.position = position;
        //intent.putExtra("position",position);

        // holder.image.setImageURI(music.album_image);
        //
        Glide.with(context)              // 0. 글라이드 사용
                .load(music.album_image) // 1. 로드할 대상 Uri
                .into(holder.image);     // 2. 입력될 이미지뷰
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView txtTitle, txtArtist;
        ImageView image;
        int position;

        public Holder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtArtist = (TextView) itemView.findViewById(R.id.txtArtist);
            image = (ImageView) itemView.findViewById(R.id.image);
            cardView.setOnClickListener(listener);
        }

        private View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        };
    }
}
