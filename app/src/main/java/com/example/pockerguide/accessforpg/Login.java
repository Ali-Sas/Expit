package com.example.pockerguide.accessforpg;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.morphingbutton.MorphingButton;
import com.example.pockerguide.MainActivity;
import com.example.pockerguide.R;
import com.example.pockerguide.animationActivity.BaseActivity;
import com.example.pockerguide.data.DATA;
import com.example.pockerguide.server.Connect;
import com.example.pockerguide.server.User;
import com.example.pockerguide.server.UserService;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends BaseActivity {
    private int mMorphCounter1 = 1;
    Intent i2;
    MorphingButton btnMorph1;
    private EditText login, password;
    SharedPreferences sp;
    String password_text = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        new Connect();
        sp = getSharedPreferences("Test",MODE_PRIVATE);

        login = (EditText) findViewById(R.id.et_login);
        password = (EditText) findViewById(R.id.et_password);
        btnMorph1 = findViewById(R.id.btn_login);
        btnMorph1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Идет поиск пользователя...",Toast.LENGTH_LONG).show();
                new MyThread().start();

            }
        });

        Intent intent = getIntent();
        String loginAdd = intent.getStringExtra("login");
        String passwordAdd = intent.getStringExtra("password");
        login.setText(loginAdd);
        password.setText(passwordAdd);

        login.addTextChangedListener(new TextWatcher() {@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {if (mMorphCounter1 != 0) {mMorphCounter1 = 0;onMorphButton1Clicked(btnMorph1);}}@Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}@Override public void afterTextChanged(Editable editable) {}});

        password.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mMorphCounter1 != 0) {
                    mMorphCounter1 = 0;onMorphButton1Clicked(btnMorph1);
                    password_text+=charSequence;
                    password.setText("*");
                }}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}});

        i2 = new Intent(this, MainActivity.class);


        morphToSquare(btnMorph1, 0);
    }

    private void onMorphButton1Clicked(final MorphingButton btnMorph) {
        if (mMorphCounter1 == 1) {
            morphToSuccess(btnMorph);
            MainActivity.SAVED_TEXT = 1;
            startActivity(i2);
        } else if (mMorphCounter1 == 2) {
            morphToFailure(btnMorph, integer(R.integer.mb_animation));
        } else {
            morphToSquare(btnMorph, integer(R.integer.mb_animation));
        }
    }

    private void morphToSquare(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params square = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.mb_corner_radius_2))
                .width(dimen(R.dimen.mb_width_200))
                .height(dimen(R.dimen.mb_height_56))
                .color(color(R.color.mb_blue))
                .colorPressed(color(R.color.mb_blue_dark))
                .text("Войти");
        btnMorph.morph(square);
    }

    private void morphToSuccess(final MorphingButton btnMorph) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(integer(R.integer.mb_animation))
                .cornerRadius(dimen(R.dimen.mb_height_56))
                .width(dimen(R.dimen.mb_height_56))
                .height(dimen(R.dimen.mb_height_56))
                .color(color(R.color.mb_green))
                .colorPressed(color(R.color.mb_green_dark));
        btnMorph.morph(circle);
    }

    private void morphToFailure(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.mb_height_56))
                .width(dimen(R.dimen.mb_height_56))
                .height(dimen(R.dimen.mb_height_56))
                .color(color(R.color.mb_red))
                .colorPressed(color(R.color.mb_red_dark))
                .icon(android.R.drawable.ic_lock_idle_lock);
        btnMorph.morph(circle);
    }

    class MyThread extends Thread{
        @Override
        public void run() {

            UserService serv = DATA.retrofit.create(UserService.class);
            Call<User> call = serv.getPerson(login.getText().toString());

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User list = response.body();
                    try {
                        if (login.getText().toString().equals(list.getLogin()) && password.getText().toString().equals(list.getPassword())) {
                            DATA.personName = list.getFirstName();
                            DATA.personSurname = list.getLastName();
                            DATA.statisticsPoints = list.getAllCoin();
                            DATA.personPoint = list.getAllCoin();
                            DATA.statisticsMuseums = list.getVisitedMuseum();
                            DATA.id = list.getId();
                            DATA.statisticsComment = list.getCommentCount();
                            DATA.login = login.getText().toString();
                            mMorphCounter1 = 1;

                        } else {
                            mMorphCounter1 = 2;
                        }
                    }catch (Exception e){
                        mMorphCounter1 = 2;
                    }
                    onMorphButton1Clicked(btnMorph1);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        login.setText(sp.getString("name",""));
        password.setText(sp.getString("family",""));
    }

    @Override
    protected void onPause() {
        super.onPause();
        String n=login.getText().toString();
        String f=password.getText().toString();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", n);
        editor.putString("family", f);
        editor.commit();
    }
}