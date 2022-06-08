package com.example.pockerguide.recycler.homepagerecycler;

public class State {

    private String name, inform;
    private String capital, coords, country;
    private String id, rating;
    String firstName, lastName;
    Integer allCoin, number;




    public State(String nameMuseum, String imageMuseum, String coordsMuseum, String infoMuseum, String id, String rating, String country) {
        this.name = nameMuseum;
        this.capital = imageMuseum;
        this.coords = coordsMuseum;
        this.inform = infoMuseum;
        this.id = id;
        this.rating = rating;
        this.country = country;
    }

    public State(String nameMuseum) {
        this.name = nameMuseum;
    }

    public State(String firstName, String lastName, Integer allCoin, Integer number) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.allCoin = allCoin;
        this.number = number;
    }

    public String getName() {
        return this.name;
    }

    public String getInform() {
        return this.inform;
    }

    public String getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return this.capital;
    }

    public String getCoords() {
        return this.coords;
    }

    public String getRating() {
        return this.rating;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Integer getAllCoin() {
        return this.allCoin;
    }

    public Integer getNumber() {
        return this.number;
    }

    public String getCountry() {
        return this.country;
    }

}
