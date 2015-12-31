package com.udacity.rahul.showstopper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class ImageAdapter extends ArrayAdapter<Movie> {
    private final Context context;
    private final ArrayList<Movie> MovieDetailList;
    private final int gridItemLayout;

    public ImageAdapter(Context context, int gridItemLayout, ArrayList<Movie> MovieDetailList) {
        super(context, R.layout.fragment_home, MovieDetailList);
        this.context = context;
        this.gridItemLayout = gridItemLayout;
        this.MovieDetailList = MovieDetailList;
    }

    public int getCount() {
        return MovieDetailList.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(gridItemLayout, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_item_image_view);
        Movie movie = getItem(position);
        String PosterUrl = movie.PosterPath;
        Picasso.with(context).load(PosterUrl).into(imageView);
        return convertView;
    }

}
