package com.example.pockerguide.soon.QRTeamAdd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pockerguide.R;
import com.example.pockerguide.soon.QRTeamAdd.QRTeam;

public class Frag2 extends Fragment {
    Button bt_chat, bt_qr_team;
    // НЕАКТИВ
    // БУДУЩАЯ РАЗРАБОТКА

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull
            ViewGroup container, @NonNull Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag2_layout, container, false);

        bt_chat = (Button) view.findViewById(R.id.bt_chat);

        bt_qr_team = (Button) view.findViewById(R.id.bt_qr_team);

        bt_qr_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), QRTeam.class);
                startActivity(intent);
            }
        });


        return view;
    }
}