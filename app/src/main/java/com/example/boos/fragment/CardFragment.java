package com.example.boos.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.boos.R;
import com.example.boos.adapter.CardAdapter;


public class CardFragment extends Fragment {

    private CardView cardView;
    private ImageView img;

    public static Fragment getInstance(int position) {
      CardFragment f = new CardFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        f.setArguments(args);

        return f;
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_viewpager, container, false);
        img = (ImageView) view.findViewById(R.id.imageView3);

        Glide.with(this)
                .load(R.drawable.eb)
                .placeholder(R.drawable.index)
                .into(img);
//        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        return view;
    }

    public CardView getCardView() {
        return cardView;
    }
}