package com.example.pockerguide.profile.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.pockerguide.R;

import java.util.ArrayList;
import java.util.List;

public class StateAdapterProfile  extends RecyclerView.Adapter<StateAdapterProfile.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<StateProfile> states;
    private Context context;
    private final OnStateClickListener onClickListener;

    public StateAdapterProfile(Context context, ArrayList<StateProfile> states, OnStateClickListener onClickListener) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.onClickListener = onClickListener;

    }

    @Override
    public StateAdapterProfile.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.profile_list_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        StateProfile state = states.get(position);

        holder.nameLevel.setText(state.getNameLevel());
        holder.nameBonus.setText(state.getNameBonus());
        holder.nameActive.setText(state.getNameActive());
        holder.iv_profile_imageLevel.setText(state.getPrivelegi());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onClickListener.onStateClick(state, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameLevel,nameBonus, nameActive, iv_profile_imageLevel;
        ViewHolder(View view){
            super(view);
            nameLevel = view.findViewById(R.id.tv_profile_nameLevel);
            nameBonus = view.findViewById(R.id.tv_profile_nameBall);
            nameActive = view.findViewById(R.id.tv_profile_namePrivelegi);
            iv_profile_imageLevel = view.findViewById(R.id.iv_profile_imageLevel);


        }
    }
    public interface OnStateClickListener{
        void onStateClick(StateProfile state, int position);
    }
}