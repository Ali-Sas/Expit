package com.example.pockerguide.recycler.homepagerecycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pockerguide.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StateAdapterCountry extends RecyclerView.Adapter<StateAdapterCountry.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<State> states;
    private Context context;





    public StateAdapterCountry(Context context, List<State> states) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
        this.context = context;


    }




    @Override
    public StateAdapterCountry.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.number_list_adapter_country, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(StateAdapterCountry.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        State state = states.get(position);

        holder.firstName.setText(String.valueOf(state.getNumber())+". "+state.getFirstName()+" "+state.getLastName());
        holder.allCoin.setText(String.valueOf(state.getAllCoin()));

    }


    @Override
    public int getItemCount() {
        return states.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView firstName, allCoin;
        ViewHolder(View view){
            super(view);
            firstName = view.findViewById(R.id.tv_firstname_top);
            allCoin = view.findViewById(R.id.tv_allcoin_top);

        }
    }

    public interface OnStateClickListener{
        void onStateClick(State state, int position);
    }


}