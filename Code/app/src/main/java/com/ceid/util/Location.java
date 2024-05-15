package com.ceid.util;

import android.os.Parcelable;
import android.os.Parcel;

import java.io.Serializable;

public class Location extends Coordinates implements Serializable {

    private final String address;

    public Location(double lat, double lng, String AddressName) {
        super(lat, lng);
        this.address = AddressName;
    }

    public Location(Coordinates coords, String AddressName) {
        super(coords);
        this.address = AddressName;
    }

    public String getAddress(){
        return address;
    }

    //Parcelable
    //=========================================================================

    /*
    //Step1
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeString(address);
    }

    //Step2
    private Location(Parcel in)
    {
        super(in.readDouble(), in.readDouble());

        this.address = in.readString();
    }

    //Step3
    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    //Step4
    @Override
    public int describeContents() {
        return 0;
    }

     */

}
