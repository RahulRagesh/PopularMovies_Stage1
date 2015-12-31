package com.udacity.rahul.showstopper;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    public String PosterPath;
    public String BackDropPath;
    public String OriginalTitle;
    public String Overview;
    public String ReleaseDate;
    public String Ratings;
    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in) {
        PosterPath =in.readString();
        BackDropPath = in.readString();
        OriginalTitle = in.readString();
        Overview = in.readString();
        ReleaseDate =in.readString();
        Ratings = in.readString();
    }
    public Movie(String PosterPath, String BackDropPath, String OriginalTitle, String Overview, String ReleaseDate, String Ratings){
        this.PosterPath = PosterPath;
        this.BackDropPath = BackDropPath;
        this.OriginalTitle = OriginalTitle;
        this.Overview = Overview;
        this.ReleaseDate = ReleaseDate;
        this.Ratings = Ratings;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(PosterPath);
        dest.writeString(BackDropPath);
        dest.writeString(OriginalTitle);
        dest.writeString(Overview);
        dest.writeString(ReleaseDate);
        dest.writeString(Ratings);
    }

    @Override
    public String toString() {
        return PosterPath + ",," + BackDropPath + ",," + OriginalTitle + ",," + Overview + ",," + ReleaseDate + ",," + Ratings;
    }
}
