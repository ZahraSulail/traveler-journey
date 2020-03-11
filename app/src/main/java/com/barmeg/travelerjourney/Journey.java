package com.barmeg.travelerjourney;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;


import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Journey implements Parcelable {
    private String title;
    private String description;
    private String photo;
    private Timestamp date;
    private GeoPoint location;

    public Journey() {
    }


    public Journey(String title, String description, String photo, Timestamp date, GeoPoint location) {

        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.photo = photo;
    }

    protected Journey(Parcel in) {
        title = in.readString();
        description = in.readString();
        photo = in.readString();
        date = in.readParcelable( Timestamp.class.getClassLoader() );
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString( title );
        dest.writeString( description );
        dest.writeString( photo );
        dest.writeParcelable( date, flags );
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Journey> CREATOR = new Creator<Journey>() {
        @Override
        public Journey createFromParcel(Parcel in) {
            return new Journey( in );
        }

        @Override
        public Journey[] newArray(int size) {
            return new Journey[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getFormatedDate(){
        return DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(date.toDate());

    }
}