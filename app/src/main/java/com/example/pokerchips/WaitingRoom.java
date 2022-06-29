package com.example.pokerchips;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;

import adapters.PlayersAdapter;
import adapters.PlayersViewHolder;
import adapters.PlayersViewModel;
import adapters.PlayersViewmodel_Factory;
import model.Player;

public class WaitingRoom extends AppCompatActivity {

    private TextView textoEsperando, playersIn, code_txt;
    private ProgressBar loadingView;
    private int shortAnimationDuration;
    private Context parentContext;
    private AppCompatActivity mActivity;
    private RecyclerView mRecyclerView;
    private PlayersViewModel playersViewModel;
    private String roomID, gameID;
    private WaitingRoom mApplication;
    private Button btn_empezarpartida;
    private FirebaseFirestore firestore;
    private boolean empezando;
    private int cambios = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_waiting);
        empezando = true;
        mApplication = this;
        textoEsperando = findViewById(R.id.textoEsperando);
        loadingView = findViewById(R.id.loading_spinner);
        code_txt = findViewById(R.id.codigotxt);
        mRecyclerView = findViewById(R.id.recyclerView);
        playersIn = findViewById(R.id.playersIn);
        btn_empezarpartida = findViewById(R.id.btn_startgame);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        if (getIntent().hasExtra("roomID")){
            this.roomID = getIntent().getExtras().getString("roomID");
            Log.d("ROOM", roomID);
            if (getIntent().hasExtra("gameID")){
                this.gameID = getIntent().getExtras().getString("gameID");
            }
            code_txt.setText(roomID);
            setLiveDataObservers();
        }

        btn_empezarpartida = findViewById(R.id.btn_startgame);

    }

    public void setLiveDataObservers() {
        //Subscribe the activity to the observable
        playersViewModel = new ViewModelProvider(this, new PlayersViewmodel_Factory(this.getApplication(), roomID)).get(PlayersViewModel.class);
        PlayersAdapter newAdapter = new PlayersAdapter(parentContext, new ArrayList<Player>());
        final Observer<ArrayList<Player>> observer = new Observer<ArrayList<Player>>() {
            @Override
            public void onChanged(ArrayList<Player> ac) {
                Log.d("CHANGEE", ac.toString());
                playersIn.setText(String.valueOf(ac.size()) + " Jugadores");
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



        firestore.collection("Game").document(gameID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                firestore.collection("Room").document(roomID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getLong("inPlayers") == documentSnapshot.getLong("numPlayers")){
                            Intent in = new Intent(WaitingRoom.this, Game.class);
                            loadingView.setVisibility(View.GONE);
                            textoEsperando.setText("Starting...");
                            in.putExtra("roomID", roomID);
                            in.putExtra("gameID", gameID);
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            startActivity(in);
                            finish();
                        }
                    }
                });
            }

        });


    }


}
