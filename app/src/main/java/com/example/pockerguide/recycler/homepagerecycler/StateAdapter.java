package com.example.pockerguide.recycler.homepagerecycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pockerguide.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StateAdapter  extends RecyclerView.Adapter<StateAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<State> states;
    private Context context;
    private final OnStateClickListener onClickListener;

    public StateAdapter(Context context, List<State> states, OnStateClickListener onClickListener) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.onClickListener = onClickListener;

    }


    @Override
    public StateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.number_list_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StateAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        State state = states.get(position);

        holder.nameView.setText(state.getName());
        holder.rating.setText("Средняя оценка: "+state.getRating()+"\nГород: "+state.getCountry());

        Picasso.with(context).load(state.getCapital()).into(holder.flagView);

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
        final ImageView flagView;
        final TextView nameView, rating;
        ViewHolder(View view){
            super(view);
            flagView = view.findViewById(R.id.image);
            nameView = view.findViewById(R.id.tv_number_item);
            rating = view.findViewById(R.id.tv_number_stage);

        }
    }
    public interface OnStateClickListener{
        void onStateClick(State state, int position);
    }
}