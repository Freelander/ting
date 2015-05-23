package com.music.ting.adapter;

import android.content.Context;

import com.music.ting.R;
import com.music.ting.model.Songs;

import java.util.List;

/**
 * Created by Jun on 2015/5/13.
 */
public class SongsListAdapter extends SimpleBaseAdapter<Songs> {

    private int page = 1;
    private int maxPage = 10;
    private List<Songs> data;

    public SongsListAdapter(Context context, int layoutId, List<Songs> data) {
        super(context, layoutId, data);
        this.data = data;
    }

    public void setPage(int page){
        this.page = page;
    }

    @Override
    public void getItemView(ViewHolder holder, Songs songs) {
        holder.setText(R.id.song_title,songs.getTitle())
                .setText(R.id.song_artist,songs.getArtist())
                .setText(R.id.song_content,songs.getContent())
                .setImageURL(R.id.song_image,songs.getUrlPic());

    }


    //    private Context mContext;
//    private List<Songs> songsList;
//    private LayoutInflater mInflater;
//
//    public SongsListAdapter(Context mContext, List<Songs> songsList){
//        this.mContext = mContext;
//        this.songsList = songsList;
//        mInflater = LayoutInflater.from(mContext);
//    }
//    @Override
//    public int getCount() {
//        return songsList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return songsList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        final ViewHolder viewHolder;
//        if(convertView == null){
//            convertView = mInflater.inflate(R.layout.item_songs_list,parent,false);
//            viewHolder = new ViewHolder(convertView);
//            convertView.setTag(viewHolder);
//        }else{
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//
//
//        viewHolder.songTitle.setText(songsList.get(position).getTitle());
//        viewHolder.songArtist.setText(songsList.get(position).getArtist());
//        Picasso.with(mContext)
//                .load(songsList.get(position).getUrlPic())
//                .placeholder(R.drawable.ic_default)
//                .error(R.drawable.ic_default)
//                .resize(80,80)
//                .centerCrop()
//                .into(viewHolder.roundImageView);
//        return convertView;
//    }
//
//    public final class ViewHolder{
//        RoundImageView roundImageView;
//        TextView songTitle;
//        TextView songArtist;
//
//        ViewHolder(View view){
//            roundImageView = (RoundImageView) view.findViewById(R.id.song_image);
//            songTitle = (TextView) view.findViewById(R.id.song_title);
//            songArtist = (TextView) view.findViewById(R.id.song_artist);
//        }
//    }
}
