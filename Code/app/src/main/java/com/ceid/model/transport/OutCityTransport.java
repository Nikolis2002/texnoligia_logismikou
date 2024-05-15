package com.ceid.model.transport;

import android.os.Parcel;
import android.os.Parcelable;

import com.ceid.model.payment_methods.Currency;
import com.ceid.model.payment_methods.CurrencyType;
import com.ceid.util.Coordinates;
import com.ceid.util.DateFormat;
import com.ceid.util.Location;
import com.ceid.util.PositiveInteger;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class OutCityTransport extends Transport {

    private String license_plate;

    public OutCityTransport(String license_plate, int id, String model, String manufacturer, String manuf_year) {
        super(id, model, manufacturer, manuf_year);
        this.license_plate = license_plate;
    }

    public String getLicensePlate()
    {
        return license_plate;
    }

    //Parcelable
    //=========================================================================

    /*
    //Step1
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.license_plate);
        dest.writeInt(this.getId());
        dest.writeString(this.getModel());
        dest.writeString(this.getManufacturer());
        dest.writeString(this.getManufYear());
    }

    //Step2
    private OutCityTransport(Parcel in)
    {
        this(in.readString(), in.readInt(), in.readString(), in.readString(), in.readString());
    }

    //Step3
    public static final Parcelable.Creator<OutCityTransport> CREATOR = new Parcelable.Creator<OutCityTransport>() {
        @Override
        public OutCityTransport createFromParcel(Parcel source) {

            switch(source.readInt())
            {
                case 0: return new OutCityCar(source);
                default: return null;
            }
        }

        @Override
        public OutCityTransport[] newArray(int size) {
            return new OutCityTransport[size];
        }
    };

    //Step4
    @Override
    public int describeContents() {
        return 0;
    }

     */
}
