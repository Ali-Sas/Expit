package com.example.pockerguide.accessforpg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.morphingbutton.MorphingButton;
import com.example.pockerguide.MainActivity;
import com.example.pockerguide.R;
import com.example.pockerguide.animationActivity.BaseActivity;
import com.example.pockerguide.data.DATA;
import com.example.pockerguide.server.User;
import com.example.pockerguide.server.UserService;

import java.io.IOException;
import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Request extends BaseActivity {
    TextView request_textName,request_textSurName,request_textEmail,request_textLogin,request_textPassword,request_textRepeatPassword, request_email;
    ImageButton ib_request_button;
    EditText request_name,request_surname,request_login,request_password,repeat_password;
    private int mMorphCounter1 = 0;
    MorphingButton btnMorph1;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        sp = getSharedPreferences("Test",MODE_PRIVATE);



        request_name = (EditText) findViewById(R.id.request_name);
        request_surname = (EditText) findViewById(R.id.request_surname);
        request_login = (EditText) findViewById(R.id.request_login);
        request_password = (EditText) findViewById(R.id.request_password);
        repeat_password = (EditText) findViewById(R.id.repeat_password);
        request_email = (EditText) findViewById(R.id.request_email);


        btnMorph1 = findViewById(R.id.btn_request);


        View.OnClickListener onClickListener = new View.OnClickListener() { // один обработчик на несколько кнопок



            @Override
            public void onClick(View view) {
                switch (view.getId()) {

                    case R.id.btn_request: // обработка 2 кнопки
                        if(request_name.getText().toString().matches("") || request_surname.getText().toString().matches("") || request_surname.getText().toString().matches("") || request_login.getText().toString().matches("") || request_password.getText().toString().matches("") || repeat_password.getText().toString().matches("")) {
//                            if (request_name.getText().toString().matches("")) {
//                                request_textName.setBackgroundColor(Color.RED);
//                            }else{
//                                request_textName.setBackgroundColor(Color.GREEN);
//                            }
//                            if (request_surname.getText().toString().matches("")) {
//                                request_textSurName.setBackgroundColor(Color.RED);
//                            }else{
//                                request_textSurName.setBackgroundColor(Color.GREEN);
//                            }
//                            if (request_email.getText().toString().matches("")) {
//                                request_textEmail.setBackgroundColor(Color.RED);
//                            }else{
//                                request_textEmail.setBackgroundColor(Color.GREEN);
//                            }
//                            if (request_login.getText().toString().matches("")) {
//                                request_textLogin.setBackgroundColor(Color.RED);
//                            }else{
//                                request_textLogin.setBackgroundColor(Color.GREEN);
//                            }
//                            if (request_password.getText().toString().matches("")) {
//                                request_textPassword.setBackgroundColor(Color.RED);
//                            }else{
//                                request_textPassword.setBackgroundColor(Color.GREEN);
//                            }
//                            if (repeat_password.getText().toString().matches("")) {
//                                request_textRepeatPassword.setBackgroundColor(Color.RED);
//                            }else{
//                                request_textRepeatPassword.setBackgroundColor(Color.GREEN);
//                            }
                            mMorphCounter1 = 2;
                            onMorphButton1Clicked(btnMorph1);



                            Toast.makeText(getApplicationContext(), "Нужно заполнить все ячейки", Toast.LENGTH_LONG).show();

                        }else{

                            if (request_password.getText().toString().equals(repeat_password.getText().toString())) {
                                ConnectLogin(request_login.getText().toString());



                            } else {
                                request_textName.setBackgroundColor(Color.GREEN);
                                request_textSurName.setBackgroundColor(Color.GREEN);
                                request_textEmail.setBackgroundColor(Color.GREEN);
                                request_textLogin.setBackgroundColor(Color.GREEN);
                                request_textPassword.setBackgroundColor(Color.RED);
                                request_textRepeatPassword.setBackgroundColor(Color.RED);

                                onMorphButton1Clicked(btnMorph1);
                                mMorphCounter1 = 2;

                                Toast.makeText(getApplicationContext(), "Пароли не совпадают", Toast.LENGTH_LONG).show();
                            }
                        }
                        break;


                    default:
                        throw new IllegalStateException("Unexpected value: " + view.getId());
                }
            }
        };

        btnMorph1.setOnClickListener(onClickListener);
    }
    @Override
    protected void onResume() {
        super.onResume();
        request_name.setText(sp.getString("nameReq",""));
        request_surname.setText(sp.getString("surname",""));
        request_email.setText(sp.getString("number",""));
        request_login.setText(sp.getString("login",""));
        request_password.setText(sp.getString("password",""));
        repeat_password.setText(sp.getString("passwordRepeat",""));
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("nameReq", request_name.getText().toString());
        editor.putString("surname", request_surname.getText().toString());
        editor.putString("number", request_email.getText().toString());
        editor.putString("login", request_login.getText().toString());
        editor.putString("password", request_password.getText().toString());
        editor.putString("passwordRepeat", repeat_password.getText().toString());
        editor.commit();
    }
    private void onMorphButton1Clicked(final MorphingButton btnMorph) {
        if (mMorphCounter1 == 1) {
            morphToSuccess(btnMorph);
            MainActivity.SAVED_TEXT = 1;
        }else if(mMorphCounter1 == 2){
            morphToFailure(btnMorph, integer(R.integer.mb_animation));
        }else{

        }

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

    public void ConnectRequest(){

        UserService serv = DATA.retrofit.create(UserService.class);
        Call<Void> call = serv.createUser(request_login.getText().toString(),request_name.getText().toString(),request_surname.getText().toString(),request_email.getText().toString(),request_password.getText().toString(),0,0,0);
        class MyThread extends Thread {
            @Override
            public void run() {
                try {
                    call.execute();
                    onMorphButton1Clicked(btnMorph1);
                    mMorphCounter1 = 1;

                    Intent intent = new Intent(getApplicationContext(),Login.class);
                    intent.putExtra("login",request_login.getText().toString());
                    intent.putExtra("password",request_password.getText().toString());
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        new MyThread().start();
    }

    public void ConnectLogin(String login){
        UserService serv = DATA.retrofit.create(UserService.class);
        Call<LinkedList<User>> call = serv.getMsg();
        call.enqueue(new Callback<LinkedList<User>>() {
            @Override
            public void onResponse(Call<LinkedList<User>> call, Response<LinkedList<User>> response) {
                LinkedList<User> user = response.body();
                boolean isPlay = true;
                for(User user2: user){
                    if(user2.getLogin().equals(login)){
                        Toast.makeText(getApplicationContext(),"Логин занят",Toast.LENGTH_SHORT).show();
                        isPlay = false;
                    }
                }
                if(isPlay) ConnectRequest();
            }

            @Override
            public void onFailure(Call<LinkedList<User>> call, Throwable t) {

            }
        });
    }
}