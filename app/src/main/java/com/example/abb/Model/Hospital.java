package com.example.abb.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Hospital implements Parcelable, Serializable {
    private String hospitalName, hospitalLocation, hospitalContacts;

    public Hospital(){

    }

    public Hospital(String hospitalName, String hospitalLocation, String hospitalContacts) {
        this.hospitalName = hospitalName;
        this.hospitalLocation = hospitalLocation;
        this.hospitalContacts = hospitalContacts;
    }


    protected Hospital(Parcel in) {
        hospitalName = in.readString();
        hospitalLocation = in.readString();
        hospitalContacts = in.readString();
    }

    public static final Creator<Hospital> CREATOR = new Creator<Hospital>() {
        @Override
        public Hospital createFromParcel(Parcel in) {
            return new Hospital(in);
        }

        @Override
        public Hospital[] newArray(int size) {
            return new Hospital[size];
        }
    };

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalLocation() {
        return hospitalLocation;
    }

    public void setHospitalLocation(String hospitalLocation) {
        this.hospitalLocation = hospitalLocation;
    }

    public String getHospitalContacts() {
        return hospitalContacts;
    }

    public void setHospitalContacts(String hospitalContacts) {
        this.hospitalContacts = hospitalContacts;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "hospitalName='" + hospitalName + '\'' +
                ", hospitalLocation='" + hospitalLocation + '\'' +
                ", hospitalContacts='" + hospitalContacts + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hospitalName);
        dest.writeString(hospitalLocation);
        dest.writeString(hospitalContacts);
    }
}
