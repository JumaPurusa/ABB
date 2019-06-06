package com.example.abb.Model;

public class Selection {

    private int position;
    private String actionImage;
    private String actionText;

    public static Selection[] selections = {
            new Selection(0,"gallery", "Select from Gallery"),
            new Selection(1,"photo_camera", "Take a Photo"),
            new Selection(2,"delete","Remove Photo")
    };

    public Selection(int position, String actionImage, String actionText) {
        this.position = position;
        this.actionImage = actionImage;
        this.actionText = actionText;
    }

    public int getPosition() {
        return position;
    }

    public String getActionImage() {
        return actionImage;
    }

    public void setActionImage(String actionImage) {
        this.actionImage = actionImage;
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }
}
