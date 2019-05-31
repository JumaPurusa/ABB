package com.example.abb.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Donor implements Parcelable, Serializable{
    private String location_name;
    private String name;
    private String email;
    private String phone_no;
    private String blood_group;

    public Donor(){

    }

    public Donor(String location_name, String name, String email, String phone_no,
                 String blood_group){
        this.location_name = location_name;
        this.name = name;
        this.email = email;
        this.phone_no = phone_no;
        this.blood_group = blood_group;
    }

    protected Donor(Parcel in) {
        location_name = in.readString();
        name = in.readString();
        email = in.readString();
        phone_no = in.readString();
        blood_group = in.readString();
    }

    public static final Creator<Donor> CREATOR = new Creator<Donor>() {
        @Override
        public Donor createFromParcel(Parcel in) {
            return new Donor(in);
        }

        @Override
        public Donor[] newArray(int size) {
            return new Donor[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(location_name);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phone_no);
        dest.writeString(blood_group);
    }
}
