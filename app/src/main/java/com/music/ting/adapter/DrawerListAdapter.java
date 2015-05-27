package com.music.ting.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import com.music.ting.R;
import com.music.ting.model.DrawerListBean;

import java.util.List;

/**
 * Created by Jun on 2015/3/25.
 *
 */
public class DrawerListAdapter extends SimpleBaseAdapter<DrawerListBean> {

    private int selectedPosition = -1;

    public DrawerListAdapter(Context context, int layoutId, List<DrawerListBean> data) {
        super(context, layoutId, data);
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    @Override
    public void getItemView(ViewHolder holder, DrawerListBean drawerListBean) {
        holder.setText(R.id.drawer_list_title,drawerListBean.getTitle())
                .setImageResource(R.id.drawer_list_image,drawerListBean.getTitleImageId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
                layoutId, position);

        getItemView(holder,mData.get(position));

        if(selectedPosition == position || (selectedPosition == -1 && position == 0)){
            holder.getView(R.id.drawer_list_title).setSelected(true);
            holder.getView(R.id.drawer_list_image).setSelected(true);
        }else{
            holder.getView(R.id.drawer_list_title).setSelected(false);
            holder.getView(R.id.drawer_list_image).setSelected(false);
            holder.getView(R.id.navigation_list_layout).setBackgroundColor(Color.TRANSPARENT);
        }

        return holder.getConvertView();
    }


}
