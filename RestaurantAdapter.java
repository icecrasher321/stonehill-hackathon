package com.mancj.example.custom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.mancj.example.R;

import com.mancj.example.custom.RestaurantActivity;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    public int i=1;
    public ArrayList<String> appName;
    Context ctx;
    boolean vis;
    String[] array=new String[3];

    public void setNames(ArrayList<String> names) {
        this.appName=names;
    }

    @NonNull
    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.restaurant_row, viewGroup, false);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ctx.startActivity(new Intent(ctx, RestaurantActivity.class));
                return false;
            }
        });
        if(i%5==1) {
            v.setBackgroundColor(Color.parseColor("#6200EE"));
        }
        if(i%5==2) {
            v.setBackgroundColor(Color.parseColor("#3700B3"));
        }
        if(i%5==3) {
            v.setBackgroundColor(Color.parseColor("#03DAC5"));
        }
        i++;
        return new RestaurantAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RestaurantAdapter.ViewHolder viewHolder, final int position) {
        String name=appName.get(position);
        viewHolder.restaurantName.setText(name);
        if(vis) {
            viewHolder.change.setVisibility(View.VISIBLE);
            viewHolder.change.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                    if (buttonView.isPressed()) {
                        viewHolder.change.setChecked(isChecked);
                        if(isChecked) {
                            array[position] = "true";
                        }
                        else{
                            array[position] = "false";
                        }
                    }
                }
            });
        }
        Log.d("MainActivity","restaurant name:"+name);
    }

    @Override
    public int getItemCount() {
        return appName.size();
    }

    public RestaurantAdapter(Context context, boolean visible) {
        ctx=context;
        vis=visible;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView restaurantName;
        SwitchCompat change;

        public ViewHolder(View itemView) {
            super(itemView);
            restaurantName= itemView.findViewById(R.id.restaurant_name);
            change=itemView.findViewById(R.id.app_switch);

        }
    }
}




