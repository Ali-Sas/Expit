package com.example.pockerguide.server;

import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MuseumService {

    @GET("/museum/getmuseums")
    public Call<LinkedList<Museums>> getMuseums();

    @GET("/museum/getmuseumcountry")
    public Call<LinkedList<Museums>> getMuseumFilter(@Query("countryMuseum") String countryMuseum);

    @GET("/museum/getmuseumstop")
    public Call<LinkedList<Museums>> getMuseumsTop();

    @GET("/museum/getmuseumsortdown")
    public Call<LinkedList<Museums>> getMuseumSortDown(@Query("countryMuseum") String countryMuseum);

    @GET("/museum/getmuseumsortup")
    public Call<LinkedList<Museums>> getMuseumSortUp(@Query("countryMuseum") String countryMuseum);

    @GET("/museum/getmuseumsortdownnull")
    public Call<LinkedList<Museums>> getMuseumSortDownNull();

    @GET("/museum/getmuseumsortupnull")
    public Call<LinkedList<Museums>> getMuseumSortUpNull();

    @GET("/museum/getmuseumsnull")
    public Call<LinkedList<Museums>> getMuseumsNull(@Query("countryMuseum") String countryMuseum);

    @GET("/museum/update")
    public Call<Void> updateMuseum(@Query("estimation") String estimation,@Query("id") Integer id);
}
