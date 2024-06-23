package com.example.finalproject.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.example.finalproject.R;
import com.example.finalproject.model.RecyclerViewAdapter;
import com.example.finalproject.model.SubmenuPreviewCard;

public abstract class
BaseSubMenuActivity extends AppCompatActivity  {
    protected RecyclerView recyclerView;
    protected RecyclerViewAdapter adapter;
    protected RecyclerView.LayoutManager layoutManager;
    protected ArrayList<SubmenuPreviewCard> cardsList;
    protected ImageButton newEntryButton;
    String tableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    protected void initialize() {
        Log.d("BaseSubmenuActivity", "initialize() called");
        cardsList = new ArrayList<>();
        newEntryButton = findViewById(R.id.newEntryButton);
    }

    //initialize RecyclerView
    protected void initializeCards() {
        recyclerView = findViewById(R.id.cardsRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(cardsList);
        recyclerView.setAdapter(adapter);
        //handle clicks for preview cards in the submenu
        adapter.setOnItemClickListener(this::onPreviewCardClick);
    }

    //run different code based on which type of card was clicked
    protected abstract void onPreviewCardClick(int position);
}