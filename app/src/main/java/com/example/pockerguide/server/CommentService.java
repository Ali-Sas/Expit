package com.example.pockerguide.server;

import com.example.pockerguide.museumactivity.recycler.Comment;

import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CommentService {

    @GET("/comment/getcomment")
    public Call<LinkedList<Comment>> getComments(@Query("id") Integer id);

    @GET("/comment/create")
    public Call<Void> createComment(@Query("firstName") String firstName, @Query("lastName") String lastName, @Query("comment") String comment,@Query("estimationMuseum") Double estimation, @Query("museumId") Integer museumId, @Query("date") String date);


}
