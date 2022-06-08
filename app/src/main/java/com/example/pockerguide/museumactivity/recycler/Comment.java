package com.example.pockerguide.museumactivity.recycler;

public class Comment {
    public Integer id;
    public String firstName, lastName, comment, date;
    public Integer museumId;
    public double estimationMuseum;
    public Comment(String firstName, String lastName, String comment, String estimation, String date){
        this.firstName = firstName;
        this.lastName = lastName;
        this.comment = comment;
        this.estimationMuseum = Double.valueOf(estimation);
        this.date = date;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getid() {
        return this.id;
    }

    public void setIdMuseum(Integer idMuseum) {
        this.museumId = idMuseum;
    }

    public Integer getMuseumId() {
        return this.museumId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return this.comment;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEstimationMuseum(double estimationMuseum) {
        this.estimationMuseum = estimationMuseum;
    }

    public double getEstimationMuseum() {
        return this.estimationMuseum;
    }
}
