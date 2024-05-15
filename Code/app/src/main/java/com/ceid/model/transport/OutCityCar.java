package com.ceid.model.transport;

import android.os.Parcel;
import android.os.Parcelable;

import com.ceid.util.Location;

import java.io.Serializable;

public class OutCityCar extends OutCityTransport {

    public OutCityCar(String license_plate, int id, String model, String manufacturer, String manuf_year)
    {
        super(license_plate, id, model, manufacturer, manuf_year);
    }

    /*
    //Parcelable
    //=========================================================================

    //Step1
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(0);
        dest.writeString(this.getLicensePlate());
        dest.writeInt(this.getId());
        dest.writeString(this.getModel());
        dest.writeString(this.getManufacturer());
        dest.writeString(this.getManufYear());
    }

    //Step2
    OutCityCar(Parcel in)
    {
        super(in.readString(), in.readInt(), in.readString(), in.readString(), in.readString());
    }

    //Step3
    public static final Parcelable.Creator<OutCityCar> CREATOR = new Parcelable.Creator<OutCityCar>() {
        @Override
        public OutCityCar createFromParcel(Parcel source) {
            return new OutCityCar(source);
        }

        @Override
        public OutCityCar[] newArray(int size) {
            return new OutCityCar[size];
        }
    };

    //Step4
    @Override
    public int describeContents() {
        return 0;
    }

     */
}
