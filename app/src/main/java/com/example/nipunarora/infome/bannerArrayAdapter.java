package com.example.nipunarora.infome;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

/**
 * Created by nipunarora on 27/12/16.
 */
public class bannerArrayAdapter extends ArrayAdapter<Banner> {
    ImageLoader mImageLoader;
    public bannerArrayAdapter(Context context, ArrayList banners) {
        super(context, 0, banners);
        mImageLoader=VolleySingleton.getInstance(getContext())   //there can be an error at this point due to the ambiguity of the way we accessed the context
                .getImageLoader();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Banner b = getItem(position);
        Log.d("getview",b.url);
        Log.d("position",String.format("%d",position));
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bannerlistview, parent, false);
        }
        ImageView mImageView;
        mImageView = (ImageView) convertView.findViewById(R.id.b);

        String IMAGE_URL = b.url;
        if (IMAGE_URL == "NULL")
        {
            return null;
        }

    else {

// Get the ImageLoader through your singleton class.

            mImageLoader.get(IMAGE_URL, ImageLoader.getImageListener(mImageView,
                    R.drawable.imgcomingsoon, R.drawable.imgcomingsoon));

            return convertView;
        }

    }
}
