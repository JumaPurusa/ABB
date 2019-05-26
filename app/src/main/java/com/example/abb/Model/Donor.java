package com.example.abb.Model;

public class Donor {
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
}
