package com.example.pockerguide.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pockerguide.data.DATA;
import com.example.pockerguide.R;
import com.example.pockerguide.profile.recycler.StateAdapterProfile;
import com.example.pockerguide.profile.recycler.StateProfile;

import java.util.ArrayList;

public class FragProfile extends Fragment {
    private static RecyclerView recyclerView;
    private ArrayList<StateProfile> profile_level = new ArrayList<StateProfile>();
    TextView tv_userNameSurname, tv_userCountry, tv_profile_statistics_museums, tv_profile_statistics_comments, tv_profile_statistics_points;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull
            ViewGroup container, @NonNull Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_frag, container, false);
        setInit();
        tv_userNameSurname = (TextView) view.findViewById(R.id.tv_userNameSurname);
        tv_userNameSurname.setText(DATA.personName + " " + DATA.personSurname);

        tv_profile_statistics_museums = (TextView) view.findViewById(R.id.tv_profile_statistics_museums);
        tv_profile_statistics_museums.setText("Пройдено музеев: "+DATA.statisticsMuseums);

        tv_profile_statistics_comments = (TextView) view.findViewById(R.id.tv_profile_statistics_comments);
        tv_profile_statistics_comments.setText("Оставлено комментариев: "+DATA.statisticsComment);

        tv_profile_statistics_points = (TextView) view.findViewById(R.id.tv_profile_statistics_points);
        tv_profile_statistics_points.setText("Всего получено очков: "+DATA.statisticsPoints);


        recyclerView = (RecyclerView) view.findViewById(R.id.rv_profile);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        StateAdapterProfile.OnStateClickListener stateClickListener2 = new StateAdapterProfile.OnStateClickListener() {
            @Override
            public void onStateClick(StateProfile state, int position) {
            }
        };
        StateAdapterProfile adapter = new StateAdapterProfile(getContext(), profile_level, stateClickListener2);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void setInit() {
        if(DATA.personPoint < 1200){
            profile_level.add(new StateProfile("Уровень 1", "От 0\nочков", "Активен","Доступ к музеям"));
            profile_level.add(new StateProfile("Уровень 2", "От 500\nочков", "Неактивен","Вы звезда!\nВ комментариях рядом с вашим именем теперь будет звездочка"));
        }else if(DATA.personPoint >= 1200){
            profile_level.add(new StateProfile("Уровень 1", "От 0\nочков", "Нективен","Доступ к музеям"));
            profile_level.add(new StateProfile("Уровень 2", "От 500\nочков", "Активен","Вы звезда!\nВ комментариях рядом с вашим именем теперь будет звездочка"));
        }else{
            profile_level.add(new StateProfile("Уровень 1", "От 0\nочков", "Нективен","Доступ к музеям"));
            profile_level.add(new StateProfile("Уровень 2", "От 500\nочков", "Неактивен","Вы звезда!\nВ комментариях рядом с вашим именем теперь будет звездочка"));
        }


    }
}
