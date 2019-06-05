package com.example.abb.Utils;

import com.example.abb.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {

    public static User parseUser(String response){

        User user = new User();

        try {
            JSONObject responseObject = new JSONObject(response);
            JSONObject jsonObject = responseObject.getJSONArray("server_response")
                    .getJSONObject(0);

            user.setFirst_name(jsonObject.getString("first_name"));
            user.setLast_name(jsonObject.getString("last_name"));
            user.setUsername(jsonObject.getString("username"));

            if(jsonObject.getString("phone") != null)
                user.setPhone(jsonObject.getString("phone"));

            if(jsonObject.getString("profile_image") != null)
                user.setProfile_image(jsonObject.getString("profile_image"));

            return user;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
