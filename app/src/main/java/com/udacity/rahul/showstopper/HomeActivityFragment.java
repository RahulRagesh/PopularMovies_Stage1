package com.udacity.rahul.showstopper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeActivityFragment extends Fragment {
    ImageAdapter mImageAdapter;
    AlertDialog levelDialog;
    private int choice = -1;
    ArrayList<Movie> MoviesList;


    public HomeActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null || !savedInstanceState.containsKey("Movies_list")){
            MoviesList = new ArrayList<>();
            FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
            fetchMoviesTask.execute("vote_count.desc");
        }
        else {
            MoviesList = savedInstanceState.getParcelableArrayList("Movies_list");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort_by) {


            // Strings to Show In Dialog with Radio Buttons
            final CharSequence[] items = {"Most Popular", "Highest Rating"};

            // Creating and Building the Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Sort By");
            builder.setSingleChoiceItems(items, choice, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {

                    FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
                    switch (item) {
                        case 0:
                            fetchMoviesTask.execute("popularity.desc");
                            choice = 0;

                            break;
                        case 1:
                            fetchMoviesTask.execute("vote_average.desc");
                            choice = 1;
                    }
                    levelDialog.dismiss();
                }
            });

            levelDialog = builder.create();
            levelDialog.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mImageAdapter = new ImageAdapter(getActivity(), R.layout.grid_item_layout, MoviesList);

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view);
        gridView.setAdapter(mImageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movie = mImageAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailsActivity.class).putExtra(Intent.EXTRA_TEXT,movie);
                startActivity(intent);
            }

        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("Movies_list",MoviesList);
        super.onSaveInstanceState(outState);

    }


    public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {
        final String LOG_TAG = FetchMoviesTask.class.getSimpleName();


        @Override
        protected Movie[] doInBackground(String... params) {
            final String BASE_URL = "http://api.themoviedb.org/3/discover/movie";
            final String SORT_BY_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";
            final String API_KEY = "";//Insert The API key here


            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            String RawJSON;
            try {
                Uri uri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(SORT_BY_PARAM, params[0]).appendQueryParameter(API_KEY_PARAM, API_KEY).build();
                URL url = new URL(uri.toString());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                if (inputStream == null) {
                    return null;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder Buffer = new StringBuilder();
                String line;
                if ((line = bufferedReader.readLine()) != null) {
                    Buffer.append(line);
                }

                RawJSON = Buffer.toString();

                try {
                    final String RESULT = "results";
                    final String POSTER_FILE_PATH = "poster_path";
                    final String ORIGINAL_TITLE = "original_title";
                    final String OVERVIEW = "overview";
                    final String VOTE_AVERAGE = "vote_average";
                    final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
                    final String BACKDROP_FILE_PATH = "backdrop_path";
                    final String RELEASE_DATE = "release_date";
                    JSONObject Movies = new JSONObject(RawJSON);
                    JSONArray MoviesList = Movies.getJSONArray(RESULT);
                    int Count = MoviesList.length();
                    Movie[] AllMovies = new Movie[Count];
                    for (int i = 0; i < Count; i++) {
                        JSONObject MovieDetails = MoviesList.getJSONObject(i);
                        AllMovies[i] = new Movie(IMAGE_BASE_URL + MovieDetails.getString(POSTER_FILE_PATH),
                                IMAGE_BASE_URL + MovieDetails.getString(BACKDROP_FILE_PATH),
                                MovieDetails.getString(ORIGINAL_TITLE),MovieDetails.getString(OVERVIEW),
                                MovieDetails.getString(RELEASE_DATE),
                                MovieDetails.getString(VOTE_AVERAGE));

                    }
                    return AllMovies;
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                    return null;
                }


            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
                return null;
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }

                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, e.getMessage(), e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Movie[] Movies) {
            super.onPostExecute(Movies);
            if (Movies != null) {
                MoviesList = new ArrayList<>(Arrays.asList(Movies));
                mImageAdapter.clear();
                mImageAdapter.addAll(MoviesList);
                mImageAdapter.notifyDataSetChanged();
            }
        }
    }
}
