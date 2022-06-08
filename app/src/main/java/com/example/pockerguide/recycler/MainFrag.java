package com.example.pockerguide.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pockerguide.R;
import com.example.pockerguide.data.DATA;
import com.example.pockerguide.museumactivity.MuseumActivity;
import com.example.pockerguide.recycler.homepagerecycler.StateAdapter;
import com.example.pockerguide.recycler.homepagerecycler.StateAdapterCountry;
import com.example.pockerguide.server.MuseumService;
import com.example.pockerguide.server.Museums;
import com.example.pockerguide.recycler.homepagerecycler.State;
import com.example.pockerguide.server.User;
import com.example.pockerguide.server.UserService;

import java.util.ArrayList;
import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFrag extends Fragment {
    public static RecyclerView recyclerView2,recyclerView;
    public static ArrayList<State> states_country = new ArrayList<State>();
    public static ArrayList<State> states_country_top = new ArrayList<State>();
    ArrayList<State> states_rating = new ArrayList<State>();
    private TextView tv_userNameSurname;
    StateAdapter adapter;
    StateAdapterCountry adapter2;
    TextView tv_serLevel, tv_ratingTop;
    SharedPreferences sp;


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_frag_layout, container, false);
        states_country.clear();
        states_country_top.clear();
        sp = getActivity().getSharedPreferences("Test", Context.MODE_WORLD_READABLE);
        new MyThread().start();
        new MyThreadUser().start();


        SharedPreferences.Editor editor = sp.edit();
        editor.putString("nameMuseum", "");
        editor.apply();

        tv_serLevel = (TextView) view.findViewById(R.id.tv_userLevel);
        tv_serLevel.setText(String.valueOf("Всего очков: "+DATA.personPoint));

        tv_ratingTop = (TextView) view.findViewById(R.id.tv_ratingTop);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_numbers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        StateAdapter.OnStateClickListener stateClickListener2 = new StateAdapter.OnStateClickListener() {
            @Override
            public void onStateClick(State state, int position) {
                Intent i = new Intent(getContext(), MuseumActivity.class);
                i.putExtra("name", state.getName());
                i.putExtra("inform", state.getInform());
                i.putExtra("image", states_country.get(position).getCapital());
                i.putExtra("coords", state.getCoords());
                i.putExtra("idMuseum",state.getId());
                Log.d("MyLog",state.getId());
                startActivity(i);
            }
        };
        adapter = new StateAdapter(getContext(), states_country_top, stateClickListener2);
        recyclerView.setAdapter(adapter);

        tv_userNameSurname = (TextView) view.findViewById(R.id.tv_userNameSurname);
        tv_userNameSurname.setText(DATA.personName +"\n"+DATA.personSurname);





        recyclerView2 = (RecyclerView) view.findViewById(R.id.rv_numbers_country);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView2.setLayoutManager(layoutManager);

        adapter2 = new StateAdapterCountry(getContext(), states_rating);

        recyclerView2.setAdapter(adapter2);
        return view;
    }

    class MyThread extends Thread{
        @Override
        public void run() {

            MuseumService servMuseum = DATA.retrofit.create(MuseumService.class);
            Call<LinkedList<Museums>> call = servMuseum.getMuseumsTop();
            call.enqueue(new Callback<LinkedList<Museums>>() {
                @Override
                public void onResponse(@NonNull Call<LinkedList<Museums>> call, @NonNull Response<LinkedList<Museums>> response) {
                    LinkedList<Museums> list = response.body();
                    states_country_top.clear();
                    int i = 0;
                    for(Museums museums:list) {
                        i++;
                        states_country.add(new com.example.pockerguide.recycler.homepagerecycler.State(museums.getNameMuseum(), museums.getImageMuseum(), museums.getCoordsMuseum(), museums.getInfoMuseum(), museums.getId().toString(), museums.getRatingMuseum(), museums.getCountryMuseum()));
                        if(i<16){
                            states_country_top.add(new com.example.pockerguide.recycler.homepagerecycler.State(museums.getNameMuseum(), museums.getImageMuseum(), museums.getCoordsMuseum(), museums.getInfoMuseum(), museums.getId().toString(), museums.getRatingMuseum(), museums.getCountryMuseum()));
                            recyclerView.setAdapter(adapter);
                        }
                    }
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<LinkedList<Museums>> call, Throwable t) {

                }
            });
        }
    }

    class MyThreadUser extends Thread{
        @Override
        public void run() {

            UserService serv = DATA.retrofit.create(UserService.class);
            Call<LinkedList<User>> call = serv.getPersonsTop();
            call.enqueue(new Callback<LinkedList<User>>() {
                @Override
                public void onResponse(@NonNull Call<LinkedList<User>> call, @NonNull Response<LinkedList<User>> response) {
                    LinkedList<User> list = response.body();
                    states_rating.clear();
                    int i = 0;
                    for(User user:list) {
                        i++;
                        states_rating.add(new com.example.pockerguide.recycler.homepagerecycler.State(user.getFirstName(), user.getLastName(), user.getAllCoin(),i));
                        if(user.getLogin().equals(DATA.login)){
                            tv_ratingTop.setText("Место в рейтинге: "+String.valueOf(i));
                        }
                        recyclerView2.setAdapter(adapter2);
                    }
                }

                @Override
                public void onFailure(Call<LinkedList<User>> call, Throwable t) {

                }
            });
        }
    }
}

