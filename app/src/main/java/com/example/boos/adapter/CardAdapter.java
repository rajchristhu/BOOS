package com.example.boos.adapter;


import androidx.cardview.widget.CardView;

public interface CardAdapter {

    public final int MAX_ELEVATION_FACTOR = 6;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}