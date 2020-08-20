package com.example.oops.model;

public class SearchModel {
    public  String image,heading,description;

    public SearchModel() {
    }

    public SearchModel(String image, String heading,String description) {
        this.image = image;
        this.heading = heading;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }
}
