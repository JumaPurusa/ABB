package com.example.abb.Utils;

public class StringManipulation {

    // making username
    public static String expandedUsername(String username){
        String formattedString = "@" + username;
        return formattedString.replace(" ", "_").toLowerCase();
    }
}


