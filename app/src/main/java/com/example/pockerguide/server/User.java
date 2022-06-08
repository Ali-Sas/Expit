package com.example.pockerguide.server;

public class User {
    private Integer id;
    private String login, firstName, lastName, phoneNumber, password;
    private Integer visitedMuseum, allCoin, commentCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getVisitedMuseum() {
        return visitedMuseum;
    }

    public void setVisitedMuseum(Integer museumCount) {
        this.visitedMuseum = museumCount;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public Integer getAllCoin() {
        return allCoin;
    }

    public void setAllCoin(Integer allCoin) {
        this.allCoin = allCoin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public void setLastName(String name) {
        this.lastName = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setPhoneNmber(String phone) {
        this.phoneNumber = phone;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}