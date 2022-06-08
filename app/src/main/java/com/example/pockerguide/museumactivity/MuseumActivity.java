package com.example.pockerguide.museumactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pockerguide.accessforpg.Login;
import com.example.pockerguide.data.DATA;
import com.example.pockerguide.MainActivity;
import com.example.pockerguide.R;
import com.example.pockerguide.map.Frag1;
import com.example.pockerguide.map.Panorama;
import com.example.pockerguide.museumactivity.recycler.Comment;
import com.example.pockerguide.museumactivity.recycler.CommentsAdapter;
import com.example.pockerguide.recycler.homepagerecycler.State;
import com.example.pockerguide.recycler.homepagerecycler.StateAdapter;
import com.example.pockerguide.server.CommentService;
import com.example.pockerguide.server.MuseumService;
import com.example.pockerguide.server.Museums;
import com.example.pockerguide.server.User;
import com.example.pockerguide.server.UserService;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MuseumActivity extends AppCompatActivity {
    TextView museum_name_activity, museum_name_inform, et_rating_comment;
    ImageView museum_image_activity;
     Button museum_name_map, museum_name_panorama, museum_name_map_marshrut, museum_name_map_gps;
    RecyclerView recyclerView;
    CommentsAdapter adapter;
    EditText comment_Museum;
    Button bt_send;
    LinkedList<Comment> comments = new LinkedList<>();
    String id;
    double rating;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum);
        museum_name_activity = (TextView) findViewById(R.id.museum_name_activity);
        comment_Museum = (EditText) findViewById(R.id.et_comment);
        bt_send = (Button) findViewById(R.id.br_send_comment);
        sp = getSharedPreferences("Test", Context.MODE_WORLD_READABLE);
        new MyThread().start();

        museum_name_inform = (TextView) findViewById(R.id.museum_name_inform);
        museum_name_map_marshrut = (Button) findViewById(R.id.museum_name_map_marshrut);
        museum_name_panorama = (Button) findViewById(R.id.museum_name_panorama);
        museum_name_map = (Button) findViewById(R.id.museum_name_map);
        museum_name_map_gps = (Button) findViewById(R.id.museum_name_map_gps);

        et_rating_comment = (TextView) findViewById(R.id.et_rating_comment);
        registerForContextMenu(et_rating_comment);


        museum_image_activity = (ImageView) findViewById(R.id.museum_image_activity);
        Intent intent = getIntent();
        String names = intent.getStringExtra("name");
        String inform = intent.getStringExtra("inform");
        String images = intent.getStringExtra("image");
        String coords = intent.getStringExtra("coords");
        id = intent.getStringExtra("idMuseum");
        Log.d("MyLog",id);


        et_rating_comment.setText("Выберите оценку");


        Picasso.with(getApplicationContext()).load(images).into(museum_image_activity);

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if(sp.getString("nameMuseum" , "").equals(names)){
                        if(et_rating_comment.getText().toString().equals("Выберите оценку") ){
                            Toast.makeText(getApplicationContext(),"Оценка не выбрана",Toast.LENGTH_LONG).show();}
                        else {
                            if(et_rating_comment.getText().toString().length()>250){
                                Toast.makeText(getApplicationContext(),"Комментарий слишком длинный\nМаксимум 250 слов, а у вас написано " + String.valueOf(et_rating_comment.getText().toString().length()),Toast.LENGTH_LONG).show();
                            }else{
                                DATA.MUSEUM_NAME = "";
                                createCommentMuseum();
                                new MyThread().start();
                                comment_Museum.setText("");
                                et_rating_comment.setText("Выберите оценку");
                            }
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Вы не посещали данный музей",Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Вы не посещали данный музей",Toast.LENGTH_LONG).show();
                }

            }
        });


        museum_name_activity.setText(names);
        museum_name_inform.setText(inform);
        Frag1.museumLatitude = 0;
        Frag1.museumLongitude = 0;

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.museum_name_map_gps:
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.putExtra("coords", coords);
                        DATA.CHECK_GPS_MUSEUM = 1;
                        startActivity(i);
                        break;
                    case R.id.museum_name_map:
                        i = new Intent(getApplicationContext(), MainActivity.class);
                        i.putExtra("coords", coords);
                        i.putExtra("name", names);
                        DATA.CHECK_GPS=1;
                        startActivity(i);
                        break;
                    case R.id.museum_name_map_marshrut:
                        i = new Intent(getApplicationContext(), MainActivity.class);
                        i.putExtra("coords", coords);
                        DATA.MUSEUM_ACTIVITY = 1;
                        startActivity(i);
                        break;
                    case R.id.museum_name_panorama:
                        i = new Intent(getApplicationContext(), Panorama.class);
                        i.putExtra("coords", coords);
                        startActivity(i);
                        break;
                }
            }
        };

        museum_name_map.setOnClickListener(onClickListener);
        museum_name_panorama.setOnClickListener(onClickListener);
        museum_name_map_gps.setOnClickListener(onClickListener);
        museum_name_map_marshrut.setOnClickListener(onClickListener);

        recyclerView = (RecyclerView) findViewById(R.id.comment_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommentsAdapter(getApplicationContext(), comments);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()){
            case R.id.et_rating_comment:
                menu.add(0, 1, 0 , "1");
                menu.add(0, 2, 0 , "2");
                menu.add(0, 3, 0 , "3");
                menu.add(0, 4, 0 , "4");
                menu.add(0, 5, 0 , "5");
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){ // обработчик постумпления информации и определения для контекстного меню
            case 1:
                et_rating_comment.setText("Оценка: 1");
                rating = 1;
                et_rating_comment.setTextColor(Color.RED);
                break;
            case 2:
                et_rating_comment.setText("Оценка: 2");
                rating = 2;
                et_rating_comment.setTextColor(Color.RED);
                break;
            case 3:
                et_rating_comment.setText("Оценка: 3");
                rating = 3;
                et_rating_comment.setTextColor(Color.YELLOW);
                break;
            case 4:
                et_rating_comment.setText("Оценка: 4");
                rating = 4;
                et_rating_comment.setTextColor(Color.GREEN);
                break;
            case 5:
                et_rating_comment.setText("Оценка: 5");
                rating = 5;
                et_rating_comment.setTextColor(Color.GREEN);
                break;
        }
        return super.onContextItemSelected(item);
    }
    void createCommentMuseum(){
        Date currentDate = new Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);

        CommentService servComment = DATA.retrofit.create(CommentService.class);
        Call<Void> call = null;
        if(DATA.statisticsPoints>=1200) {
            call = servComment.createComment("⭐ "+DATA.personName,DATA.personSurname,comment_Museum.getText().toString(),rating,Integer.parseInt(id), timeText+" "+dateText);
        }else{
            call = servComment.createComment(DATA.personName,DATA.personSurname,comment_Museum.getText().toString(),rating,Integer.parseInt(id), timeText+" "+dateText);
        }


        double rating2 = rating;
        for(Comment comment:comments){
            rating2+=comment.getEstimationMuseum();
        }

        MuseumService servMuseum = DATA.retrofit.create(MuseumService.class);

        rating2 = rating2/(comments.size()+1);
        Call<Void> call3 = servMuseum.updateMuseum(String.valueOf(rating2),Integer.parseInt(id));

        class MyThreadMuseum extends Thread {
            @Override
            public void run() {
                try {
                    call3.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        new MyThreadMuseum().start();


        UserService servUser = DATA.retrofit.create(UserService.class);

        Call<Void> call2 = servUser.updatePerson(DATA.id,DATA.statisticsMuseums,DATA.statisticsPoints+100,DATA.statisticsComment+1);
        DATA.statisticsComment+=1;
        DATA.statisticsPoints+=100;
        Call<Void> finalCall = call;
        class MyThreadComment extends Thread {
            @Override
            public void run() {
                try {
                    finalCall.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        new MyThreadComment().start();

        class MyThreadUpdate extends Thread {
            @Override
            public void run() {
                try {
                    call2.execute();
                    new MyThread().start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        new MyThreadUpdate().start();
    }
        class MyThread extends Thread{
            @Override
            public void run() {

                CommentService servComment = DATA.retrofit.create(CommentService.class);
                Call<LinkedList<Comment>> call = servComment.getComments(Integer.parseInt(id));

                call.enqueue(new Callback<LinkedList<Comment>>() {
                    @Override
                    public void onResponse(Call<LinkedList<Comment>> call, Response<LinkedList<Comment>> response) {
                        LinkedList<Comment> comment = response.body();
                        comments.clear();
                        try {
                            for (Comment a : comment) {
                                comments.add(new Comment(a.getFirstName(), a.getLastName(), a.getComment(), String.valueOf(a.getEstimationMuseum()), a.getDate()));
                            }
                        }catch (Exception e){

                        }
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<LinkedList<Comment>> call, Throwable t) {

                    }
                });
            }
        }
    }
