package com.example.pockerguide.museumcollection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pockerguide.R;
import com.example.pockerguide.data.DATA;
import com.example.pockerguide.museumactivity.MuseumActivity;
import com.example.pockerguide.museumcollection.recycler.StateAdapterCollection;
import com.example.pockerguide.recycler.MainFrag;
import com.example.pockerguide.recycler.homepagerecycler.State;
import com.example.pockerguide.recycler.homepagerecycler.StateAdapter;
import com.example.pockerguide.server.MuseumService;
import com.example.pockerguide.server.Museums;
import com.example.pockerguide.server.UserService;

import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragMuseumCollection extends Fragment {
    RecyclerView recyclerView;
    StateAdapterCollection adapter;
    LinkedList<State> collectionMuseum = new LinkedList<>();
    Button filter_send;
    TextView et_collection_museum_country, et_collection_museum_sort;
    String country, sort;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull
            ViewGroup container, @NonNull Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frag_museum_collection, container, false);
        collectionMuseum.add(new State("ВЫБЕРИТЕ ФИЛЬТР ИЛИ ГОРОД"));

        filter_send = (Button) view.findViewById(R.id.filter_museum);
        et_collection_museum_country = (TextView) view.findViewById(R.id.et_collection_museum_country);
        et_collection_museum_sort = (TextView) view.findViewById(R.id.et_collection_museum_sort);
        registerForContextMenu(et_collection_museum_country);
        registerForContextMenu(et_collection_museum_sort);
        country = "Все";
        sort = "Нет фильтра";

        filter_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    collectionMuseum.clear();
                    new MyThreadFilter().start();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_collection);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        StateAdapterCollection.OnStateClickListener stateClickListener2 = new StateAdapterCollection.OnStateClickListener() {
            @Override
            public void onStateClick(State state, int position) {
                Intent i = new Intent(getContext(), MuseumActivity.class);
                i.putExtra("name", state.getName());
                i.putExtra("inform", state.getInform());
                i.putExtra("image", collectionMuseum.get(position).getCapital());
                i.putExtra("coords", state.getCoords());
                i.putExtra("idMuseum",state.getId());
                startActivity(i);
            }
        };
        adapter = new StateAdapterCollection(getContext(), collectionMuseum, stateClickListener2);
        recyclerView.setAdapter(adapter);
        return view;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()){
            case R.id.et_collection_museum_country:
                menu.add(0, 7, 0 , "Все");
                menu.add(0, 1, 0 , "Москва");
                menu.add(0, 2, 0 , "Ростов");
                menu.add(0, 3, 0 , "Волгоград");
                break;
            case R.id.et_collection_museum_sort:
                menu.add(0, 4, 0 , "Нет фильтра");
                menu.add(0, 5, 0 , "По возрастанию оценки");
                menu.add(0, 6, 0 , "По убыванию оценки");
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){ // обработчик постумпления информации и определения для контекстного меню
            case 1:
                et_collection_museum_country.setText("Москва");
                country = "Москва";
                break;
            case 2:
                et_collection_museum_country.setText("Ростов");
                country = "Ростов";
                break;
            case 3:
                et_collection_museum_country.setText("Волгоград");
                country = "Волгоград";
                break;
            case 4:
                et_collection_museum_sort.setText("Нет фильтра");
                sort = "Нет фильтра";
                break;
            case 5:
                et_collection_museum_sort.setText("По возрастанию оценки");
                sort = "По возрастанию оценки";
                break;
            case 6:
                et_collection_museum_sort.setText("По убыванию оценки");
                sort = "По убыванию оценки";
                break;
            case 7:
                et_collection_museum_country.setText("Все");
                country = "Все";
                break;
        }
        return super.onContextItemSelected(item);
    }

    class MyThreadFilter extends Thread{
        @Override
        public void run() {
            collectionMuseum.clear();

            MuseumService servMuseum = DATA.retrofit.create(MuseumService.class);
            Call<LinkedList<Museums>> call = null;
            if(sort.equals("По возрастанию оценки") && country.equals("Все")){
                call = servMuseum.getMuseumSortDownNull();
            }else if(sort.equals("По убыванию оценки") && country.equals("Все")){
                call = servMuseum.getMuseumSortUpNull();
            }else if(sort.equals("Нет фильтра") && country.equals("Все")){
                call = servMuseum.getMuseums();
            }else if(sort.equals("По возрастанию оценки")){
                call = servMuseum.getMuseumSortDown(country);
            }else if(sort.equals("По убыванию оценки")){
                call = servMuseum.getMuseumSortUp(country);
            }else if(sort.equals("Нет фильтра")){
                call = servMuseum.getMuseumsNull(country);
            }



            call.enqueue(new Callback<LinkedList<Museums>>() {
                @Override
                public void onResponse(Call<LinkedList<Museums>> call, Response<LinkedList<Museums>> response) {
                    LinkedList<Museums> list = response.body();
                    for(Museums museums:list) {
                        collectionMuseum.add(new com.example.pockerguide.recycler.homepagerecycler.State(museums.getNameMuseum(), museums.getImageMuseum(), museums.getCoordsMuseum(), museums.getInfoMuseum(), museums.getId().toString(), museums.getRatingMuseum(), museums.getCountryMuseum()));
                        recyclerView.setAdapter(adapter);
                    }

                }

                @Override
                public void onFailure(Call<LinkedList<Museums>> call, Throwable t) {

                }
            });
        }
    }
}