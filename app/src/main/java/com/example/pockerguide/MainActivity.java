package com.example.pockerguide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pockerguide.accessforpg.Login;
import com.example.pockerguide.accessforpg.Request;
import com.example.pockerguide.data.DATA;
import com.example.pockerguide.map.Frag1;
import com.example.pockerguide.museumcollection.FragMuseumCollection;
import com.example.pockerguide.profile.FragProfile;
import com.example.pockerguide.recycler.MainFrag;
import com.example.pockerguide.server.Connect;
import com.example.pockerguide.server.User;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {


    public static int SAVED_TEXT = 0;

    public static LocationManager locationManager2;
    private ImageButton main, museum, map, support, profile;
    private ImageButton intent_login, intent_registration;
    public static Bitmap bit;
    public static int t = 0;
    public static WindowManager manager;
    public static Context context;

    public static FragmentManager fmm;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (SAVED_TEXT == 0) {

            setContentView(R.layout.titul_layout);

            intent_login = (ImageButton) findViewById(R.id.bt_intent_login);
            intent_registration = (ImageButton) findViewById(R.id.bt_intent_registration);

            Intent intentLogin = new Intent(this, Login.class);
            Intent intentRegistration = new Intent(this, Request.class);


            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {


                        case R.id.bt_intent_login:
                            startActivity(intentLogin);
                            break;

                        case R.id.bt_intent_registration:
                            startActivity(intentRegistration);
                            break;


                    }

                }
            };
            intent_registration.setBackgroundDrawable(null);
            intent_login.setBackgroundDrawable(null);
            intent_login.setOnClickListener(onClickListener);
            intent_registration.setOnClickListener(onClickListener);


        } else {
            setContentView(R.layout.activity_main);

             fmm = getSupportFragmentManager();


            locationManager2 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);



                FragmentManager fmm = getSupportFragmentManager();
                FragmentTransaction ftm = fmm.beginTransaction();
                MainFrag mf = new MainFrag();
                ftm.replace(R.id.frame, mf);
                ftm.commit();

                if (DATA.MUSEUM_ACTIVITY == 1) {
                    Intent intent = getIntent();
                    String coordss = intent.getStringExtra("coords");
                    Frag1.museumLatitude = Double.parseDouble(coordss.substring(0, 9));
                    Frag1.museumLongitude = Double.parseDouble(coordss.substring(11, 20));
                    DATA.CHECK_GPS=0;



                    FragmentManager fm1 = getSupportFragmentManager();
                    FragmentTransaction ft1 = fm1.beginTransaction();
                    Frag1 fb1 = new Frag1();
                    ft1.replace(R.id.frame, fb1);
                    ft1.commit();
                    fb1.getSavedStateRegistry();
                }else if(DATA.CHECK_GPS_MUSEUM==1){
                    Intent intent = getIntent();
                    String coordss = intent.getStringExtra("coords");
                    Frag1.museumLatitude = Double.parseDouble(coordss.substring(0, 9));
                    Frag1.museumLongitude = Double.parseDouble(coordss.substring(11, 20));
                    DATA.CHECK_GPS=0;


                    FragmentManager fm1 = getSupportFragmentManager();
                    FragmentTransaction ft1 = fm1.beginTransaction();
                    Frag1 fb1 = new Frag1();
                    ft1.replace(R.id.frame, fb1);
                    ft1.commit();
                    fb1.getSavedStateRegistry();
                }else if(DATA.CHECK_GPS==1){
                    Intent intent = getIntent();
                    String coordss = intent.getStringExtra("coords");
                    String name = intent.getStringExtra("name");
                    Frag1.museumLatitude = Double.parseDouble(coordss.substring(0, 9));
                    Frag1.museumLongitude = Double.parseDouble(coordss.substring(11, 20));
                    Frag1.museum_name = name;


                    FragmentManager fm1 = getSupportFragmentManager();
                    FragmentTransaction ft1 = fm1.beginTransaction();
                    Frag1 fb1 = new Frag1();
                    ft1.replace(R.id.frame, fb1);
                    ft1.commit();
                    fb1.getSavedStateRegistry();
                }



                main = (ImageButton) findViewById(R.id.main);
                museum = (ImageButton) findViewById(R.id.museum);
                map = (ImageButton) findViewById(R.id.map);
                profile = (ImageButton) findViewById(R.id.profile);

                main.setBackgroundColor(R.color.colorAccent);




                View.OnClickListener onClickListener = new View.OnClickListener() { // один обработчик на несколько кнопок


                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {


                            case R.id.main:
                                nextFragment();
                                main.setBackgroundColor(R.color.colorAccent);
                                DATA.MUSEUM_ACTIVITY = 0;

                                FragmentManager fmm = getSupportFragmentManager();
                                FragmentTransaction ftm = fmm.beginTransaction();
                                MainFrag mf = new MainFrag();
                                ftm.replace(R.id.frame, mf);
                                ftm.commit();


                                break;
                            case R.id.museum:
                                nextFragment();
                                museum.setBackgroundColor(R.color.colorAccent);
                                DATA.MUSEUM_ACTIVITY = 0;

                                FragmentManager fm = getSupportFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                FragMuseumCollection fb = new FragMuseumCollection();
                                ft.replace(R.id.frame, fb);
                                ft.commit();
                                fb.getSavedStateRegistry();




                                break;
                            case R.id.map:
                                nextFragment();
                                map.setBackgroundColor(R.color.colorAccent);
                                Frag1.museumLatitude = 0;
                                Frag1.museumLongitude = 0;
                                DATA.MUSEUM_ACTIVITY = 0;
                                DATA.CHECK_GPS=0;


                                FragmentManager fm1 = getSupportFragmentManager();
                                FragmentTransaction ft1 = fm1.beginTransaction();
                                Frag1 fb1 = new Frag1();
                                ft1.replace(R.id.frame, fb1);
                                ft1.commit();
                                fb1.getSavedStateRegistry();

                                break;
                            case R.id.profile:
                                nextFragment();
                                profile.setBackgroundColor(R.color.colorAccent);
                                DATA.MUSEUM_ACTIVITY = 0;

                                FragmentManager fm3 = getSupportFragmentManager();
                                FragmentTransaction ft3 = fm3.beginTransaction();
                                FragProfile fp = new FragProfile();
                                ft3.replace(R.id.frame, fp);
                                ft3.commit();

                                break;


                        }
                    }
                };


                main.setOnClickListener(onClickListener);
                museum.setOnClickListener(onClickListener);
                map.setOnClickListener(onClickListener);
                profile.setOnClickListener(onClickListener);
            }
        }


        private void nextFragment () {
            profile.setBackgroundColor(Color.BLUE);


            museum.setBackgroundColor(Color.BLUE);


            map.setBackgroundColor(Color.BLUE);




            main.setBackgroundColor(Color.BLUE);

        }
    }








