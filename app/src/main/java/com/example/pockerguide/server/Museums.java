package com.example.pockerguide.server;

public class Museums {
    public Integer id;
    public String nameMuseum, countryMuseum, imageMuseum, coordsMuseum, infoMuseum, ratingMuseum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNameMuseum(String name) {
        this.nameMuseum = name;
    }

    public String getNameMuseum() {
        return nameMuseum;
    }

    public void setCountryMuseum(String country) {
        this.countryMuseum = country;
    }

    public String getCountryMuseum() {
        return countryMuseum;
    }

    public void setImageMuseum(String image) {
        this.imageMuseum = image;
    }

    public String getImageMuseum() {
        return imageMuseum;
    }

    public void setCoordsMuseum(String coords) {
        this.coordsMuseum = coords;
    }

    public String getCoordsMuseum() {
        return coordsMuseum;
    }

    public void setInfoMuseum(String info) {
        this.infoMuseum = info;
    }

    public String getInfoMuseum() {
        return infoMuseum;
    }

    public void setRatingMuseum(String rating) {
        this.ratingMuseum = rating;
    }

    public String getRatingMuseum() {
        return ratingMuseum;
    }
}
