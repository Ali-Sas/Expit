package com.example.pockerguide.museumactivity.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pockerguide.R;
import com.example.pockerguide.recycler.homepagerecycler.State;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Comment> states;
    private Context context;

    public CommentsAdapter(Context context, List<Comment> states) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
        this.context = context;

    }
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.comments_list_adapter, parent, false);
        return new CommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Comment state = states.get(position);



        holder.firstName.setText(state.getFirstName()+" "+state.getLastName());
        holder.tv_comment_time.setText(state.getDate());
        if(state.getEstimationMuseum()==5.0 || state.getEstimationMuseum()==4.0) holder.ocenka.setTextColor(Color.GREEN);
        if(state.getEstimationMuseum()==1.0 || state.getEstimationMuseum()==2.0) holder.ocenka.setTextColor(Color.RED);
        if(state.getEstimationMuseum()==3.0) holder.ocenka.setTextColor(Color.YELLOW);
        holder.ocenka.setText("Оценка: "+String.valueOf(state.getEstimationMuseum()));
        holder.comment.setText(state.getComment());

    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView firstName,comment, ocenka, tv_comment_time;
        ViewHolder(View view){
            super(view);
            firstName = (TextView) view.findViewById(R.id.tv_firstName_comment);
            comment = (TextView) view.findViewById(R.id.tv_comment);
            ocenka = (TextView) view.findViewById(R.id.tv_ocenka);
            tv_comment_time = (TextView) view.findViewById(R.id.tv_comment_time);

        }
    }
    public interface OnStateClickListener{
        void onStateClick(State state, int position);
    }
}