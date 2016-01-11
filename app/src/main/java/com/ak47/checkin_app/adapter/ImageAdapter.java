package com.ak47.checkin_app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ak47.checkin_app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Dimon on 2016/1/12.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    //图片url地址（多张图片）
    private ArrayList<String> urls;

    public ImageAdapter(Context context,ArrayList<String> urls) {
        this.mContext = context;
        this.urls=urls;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int i) {
        return urls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView=new ImageView(mContext);

        //设置placeholder和error image
        Picasso.with(mContext).load(urls.get(i)).placeholder(R.mipmap.image_placeholder).error(R.mipmap.ic_launcher).into(imageView);

        //设置网格中显示imageView的属性
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(280, 280));

        return imageView;
    }

}
