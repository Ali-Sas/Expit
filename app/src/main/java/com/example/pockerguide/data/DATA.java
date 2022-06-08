package com.example.pockerguide.data;


import com.yandex.mapkit.MapKitFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DATA{
    public static String personName = "Ошибка", personSurname = "Ошибка", personLevel = "3",  login, MUSEUM_NAME = "";
    public static int statisticsPoints,statisticsMuseums,statisticsComment, personPoint, id;
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://pocket-guide2.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static int MapFrag1 = 0, MUSEUM_ACTIVITY = 0, MAINFRAG_MUSEUM=0, CHECK_GPS=0, CHECK_GPS_MUSEUM=0, TOP_USER;
}
