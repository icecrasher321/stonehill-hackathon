package com.mancj.example.custom;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mancj.example.R;

import java.util.ArrayList;
import java.util.Random;

public class RestaurantActivity extends AppCompatActivity {
    RecyclerView rvRecommendation;
    ArrayList dishes=new ArrayList<>();
    String submit="SUBMIT";
    Context ctx;
    Button thoughts;
    RatingBar rating;
    TextView text1;
    TextView text2;
    boolean click2=false;
    float rating_of;
    int i=1;
    int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        rvRecommendation=findViewById(R.id.dishes);
        thoughts=findViewById(R.id.thoughts);
        rating=findViewById(R.id.rating);
        text2=findViewById(R.id.text2);
        text1=findViewById(R.id.text);
        ctx=this;
        RestaurantAdapter adapter = new RestaurantAdapter(this, false);
        rvRecommendation.setLayoutManager(new LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false));
        rvRecommendation.setAdapter(adapter);
        Random rand = new Random();
        n = rand.nextInt(5);
        n=n+1;
        if(getSharedPreferences("firstRun", MODE_PRIVATE).getBoolean("try_new", true)) {
            if (n == 1) {
                dishes.add("Shrimp Curry");
                dishes.add("Fried Rice");
                dishes.add("Roasted Snapper");
            }
            if (n == 2) {
                dishes.add("Schezuan Chicken");
                dishes.add("Stir-fried Tofu Rice");
                dishes.add("Momos");
            }
            if (n == 3) {
                dishes.add("Tapioca Khichdi");
                    dishes.add("Signature Hiash Browns");
                dishes.add("Paneer tikka with mint chutney");
            }
            if (n == 4) {
                dishes.add("Rava Masala Dosa");
                dishes.add("Filter Coffee");
                dishes.add("Rasmalai");
            }
            if (n == 5) {
                dishes.add("Tuna 6-inch");
                dishes.add("Dark Chocolate Cookie");
                dishes.add("Potato Crisps");
            }
        }
        else {
            if (n == 1) {
                dishes.add("Fetta Cheese Salad");
                dishes.add("Bologna");
                dishes.add("Pepperoni Pizza");
            }
            if (n == 2) {
                dishes.add("Schezuan Chicken");
                dishes.add("Stir-fried Tofu Rice");
                dishes.add("Momos");
            }
            if (n == 3) {
                dishes.add("Chicken Supreme");
                dishes.add("Chocolate brownie");
                dishes.add("Strawberry Milkshake");
            }
            if (n == 4) {
                dishes.add("Steak medium rare");
                dishes.add("Ice Tea");
                dishes.add("Chocolate Bomb");
            }
            if (n == 5) {
                dishes.add("Garlic Naan");
                dishes.add("Butter Chicken");
                dishes.add("Dal Makhani");
            }
        }

        adapter.setNames(dishes);
        adapter.notifyDataSetChanged();

        thoughts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!click2) {
                    rating.setVisibility(View.VISIBLE);
                    text1.setText("Rate the restaurant");
                    text2.setVisibility(View.VISIBLE);
                    thoughts.setText(submit);
                    RestaurantAdapter adapter = new RestaurantAdapter(ctx, true);
                    rvRecommendation.setLayoutManager(new LinearLayoutManagerWrapper(ctx, LinearLayoutManager.VERTICAL, false));
                    rvRecommendation.setAdapter(adapter);
                    adapter.setNames(dishes);
                    adapter.notifyDataSetChanged();
                    rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                            rating_of=rating;
                        }
                    });
                    click2=true;
                }
                else{

                }
            }
        });

    }

    public class LinearLayoutManagerWrapper extends LinearLayoutManager {

        public LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
    }
}

