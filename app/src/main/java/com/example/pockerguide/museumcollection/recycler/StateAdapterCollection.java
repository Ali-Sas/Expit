package com.example.pockerguide.museumcollection.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pockerguide.R;
import com.example.pockerguide.recycler.homepagerecycler.State;
import com.example.pockerguide.recycler.homepagerecycler.StateAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StateAdapterCollection extends RecyclerView.Adapter<StateAdapterCollection.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<State> states;
    private Context context;
    private final OnStateClickListener onClickListener;





    public StateAdapterCollection(Context context, List<State> states, StateAdapterCollection.OnStateClickListener onClickListener) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.onClickListener = onClickListener;

    }



    @Override
    public StateAdapterCollection.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.rv_museum_filter_collection, parent, false);

        return new StateAdapterCollection.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(StateAdapterCollection.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        State state = states.get(position);

                holder.nameView.setText(state.getName());
                holder.ocenka.setText("Средняя оценка: "+state.getRating()+"\nГород: "+state.getCountry());

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
        final TextView nameView,ocenka;
        ViewHolder(View view){
            super(view);
            flagView = (ImageView) view.findViewById(R.id.image_collection);
            ocenka = (TextView) view.findViewById(R.id.tv_number_stage_collection);
            nameView = (TextView) view.findViewById(R.id.tv_number_item);
        }
    }

    public interface OnStateClickListener{
        void onStateClick(State state, int position);
    }


}