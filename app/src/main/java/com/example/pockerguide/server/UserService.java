package com.example.pockerguide.server;


import com.example.pockerguide.museumactivity.recycler.Comment;

import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {
    @GET("/user/getpersons")
    public Call<LinkedList<User>> getMsg();

    @GET("/user/create")
    public Call<Void> createUser(@Query("login") String login, @Query("firstName") String firstName, @Query("lastName") String lastName, @Query("phoneNumber") String phoneNumber, @Query("password") String password, @Query("visitedMuseum") Integer visitedMuseum, @Query("allCoin") Integer allCoin, @Query("commentCount") Integer commentCount);


    @GET("/user/getperson")
    public Call<User> getPerson(@Query("id") String login);

    @GET("/user/update")
    public Call<Void> updatePerson(@Query("id") Integer id,@Query("visitedMuseum") Integer visitedMuseum, @Query("allCoin") Integer allCoin, @Query("commentCount") Integer commentCount);

    @GET("/user/getpersonstop")
    public Call<LinkedList<User>> getPersonsTop();


}
