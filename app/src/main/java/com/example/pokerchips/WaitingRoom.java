package com.example.pokerchips;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import adapters.PlayersAdapter;
import adapters.PlayersViewHolder;
import adapters.PlayersViewModel;
import adapters.PlayersViewmodel_Factory;
import model.Player;

public class WaitingRoom extends AppCompatActivity {

    private TextView textoEsperando;
    private ProgressBar loadingView;
    private int shortAnimationDuration;
    private Context parentContext;
    private AppCompatActivity mActivity;
    private RecyclerView mRecyclerView;
    private PlayersViewModel playersViewModel;
    private String roomID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        textoEsperando = findViewById(R.id.textoEsperando);
        loadingView = findViewById(R.id.loading_spinner);
        shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        if (getIntent().hasExtra("roomID")){
            this.roomID = getIntent().getExtras().getString("roomID");
            Log.d("ROOM", roomID);
            setLiveDataObservers();
        }

    }

    public void setLiveDataObservers() {
        //Subscribe the activity to the observable
        playersViewModel = new ViewModelProvider(this, new PlayersViewmodel_Factory(this.getApplication(), roomID)).get(PlayersViewModel.class);
        PlayersAdapter newAdapter = new PlayersAdapter(parentContext, new ArrayList<Player>());
        final Observer<ArrayList<Player>> observer = new Observer<ArrayList<Player>>() {
            @Override
            public void onChanged(ArrayList<Player> ac) {
                PlayersAdapter newAdapter = new PlayersAdapter(parentContext, ac);
                mRecyclerView.swapAdapter(newAdapter, false);
                newAdapter.notifyDataSetChanged();
            }
        };

        final Observer<String> observerToast = new Observer<String>() {
            @Override
            public void onChanged(String t) {
                //Toast.makeText(parentContext, t, Toast.LENGTH_SHORT).show();
            }
        };


        playersViewModel.getPlayersCards().observe(this, observer);
        playersViewModel.getToast().observe(this, observerToast);

    }
}
