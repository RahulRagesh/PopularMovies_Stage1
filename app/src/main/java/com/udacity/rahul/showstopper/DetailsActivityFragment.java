package com.udacity.rahul.showstopper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {


    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        Intent intent = getActivity().getIntent();
        Movie MovieDetails;
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            MovieDetails = intent.getExtras().getParcelable(Intent.EXTRA_TEXT);
            if (MovieDetails != null) {
                String Ratings = MovieDetails.Ratings + " / 10";
                ImageView PosterView = (ImageView) rootView.findViewById(R.id.detail_poster);
                Picasso.with(getActivity()).load(MovieDetails.PosterPath).into(PosterView);
                ((TextView) rootView.findViewById(R.id.detail_title)).setText(MovieDetails.OriginalTitle);
                ((TextView) rootView.findViewById(R.id.detail_release_date)).setText(MovieDetails.ReleaseDate);
                ((TextView) rootView.findViewById(R.id.detail_overview)).setText(MovieDetails.Overview);
                ((TextView) rootView.findViewById(R.id.detail_votings)).setText(Ratings);
            }
        }
        return rootView;
    }
}
