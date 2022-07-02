package com.example.pokerchips;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import model.Player;

public class Game extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private String userID, roomID, gameID;
    private TextView seat1, seat2, seat3, seat4, seat5, seat6, seat7, seat8;
    private TextView chips1, chips2, chips3, chips4, chips5, chips6, chips7, chips8;
    private TextView myChips;
    private EditText mybet;
    private TextView turno;
    private int turn = 0, numPlayers;
    private ArrayList<Player> playersList;
    private ArrayList<TextView> seatsViews;
    private ArrayList<TextView> chipsView;
    private Button btn_apostar, btn_abandonar, btn_pasar;
    private boolean turn_ended = false;
    private boolean not_turn = false;
    private boolean gameStarted = false;

    private String turnID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        turno = findViewById(R.id.turn_txt);
        seatsViews = new ArrayList<TextView>();
        chipsView = new ArrayList<TextView>();
        userID = mAuth.getCurrentUser().getUid();
        turnID = "nsajdbjasbjdbajs";



        if (getIntent().hasExtra("roomID") && getIntent().hasExtra("gameID")) {
            this.roomID = getIntent().getExtras().getString("roomID");
            this.gameID = getIntent().getExtras().getString("gameID");
            firestore.collection("Game").document(gameID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(!documentSnapshot.getBoolean("started")){
                        firestore.collection("Game").document(gameID).update("started", true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                conigureViews();
                            }
                        });
                    }else{
                        conigureViews();
                    }
                }


            });
            Log.d("ROOM", roomID);
        }


    }


    public void addEventTurnUpdate(){
        firestore.collection("Game").document(gameID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(!value.getString("playerTurn").equals(turnID) && value.exists()){
                    if(turn_ended){
                        turn_ended = false;
                        setTurn();
                    }
                }

            }
        });
    }

    public void setTurn(){
        turn++;
        Log.d("Modulo", String.valueOf(turn) + " % " + String.valueOf(numPlayers));
        int a = turn % numPlayers;
        turnID = playersList.get(a).getPlayer_id();

        firestore.collection("Game").document(gameID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String turn_actual = documentSnapshot.getString("playerTurn");
                Log.d("TURNO?", turn_actual + " es igual a " + turnID + " ??");
                if (turn_actual.equals(turnID)) {
                    turno.setText("Turno de " + playersList.get(a).getUsername());
                    turnID = turn_actual;
                    if (mAuth.getCurrentUser().getUid().equals(turnID)) {
                        yourTurn();
                    }
                } else {
                    firestore.collection("Game").document(gameID).update("playerTurn", playersList.get(a)
                            .getPlayer_id()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            turno.setText("Turno de " + playersList.get(a).getUsername());
                            turnID = playersList.get(a).getPlayer_id();
                            if (mAuth.getCurrentUser().getUid().equals(turnID)){
                                yourTurn();
                            }
                        }
                    });
                }

            }
        });



    }

    public void seatPlayers(){

        firestore.collection("Room").document(roomID).collection("Player")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot value) {
                playersList = new ArrayList<>();
                for (DocumentSnapshot document : value.getDocuments()) {
                    if (document.exists()) {
                        Log.d("PLAYER", document.getData().toString());
                        Player player = new Player(document.getString("player_name"),
                                Math.toIntExact(document.getLong("player_chips")), document.getString("player_id"));
                        playersList.add(player);
                    }
                }


                for (int i = 0; i < playersList.size() ; i++) {
                    Player act = playersList.get(i);
                    if(mAuth.getCurrentUser().getUid().equals(act.getPlayer_id())){
                        TextView text = seatsViews.get(i);
                        text.setText("YOU");
                        text = chipsView.get(i);
                        text.setText(act.getUserChipstoString());
                    }else{
                        TextView text = seatsViews.get(i);
                        text.setText(act.getUsername());
                        text = chipsView.get(i);
                        text.setText(act.getUserChipstoString());
                    }

                }

                for (int i = playersList.size(); i < 8; i++) {
                    TextView text = seatsViews.get(i);
                    text.setText("");
                    text = chipsView.get(i);
                    text.setText("");
                }

            firestore.collection("Room").document(roomID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    myChips = findViewById(R.id.poker_chips_usertxt);
                    myChips.setText((String.valueOf(Math.toIntExact(task.getResult().getLong("numChips")))));
                    numPlayers = Math.toIntExact(task.getResult().getLong("inPlayers"));
                    firestore.collection("Game").document(gameID).update("playerTurn", playersList.get(0).getPlayer_id()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            firestore.collection("Game").document(gameID).update("intTurn", 0).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    startGame();
                                }
                            });
                        }
                    });

                }
            });

            }
        });
    }


    public void endOfTurn(){
        setTurn();
    }
    public void startGame(){
        addEventTurnUpdate();
        setTurn();
    }
    private void yourTurn() {
        btn_pasar.setEnabled(true);
        btn_abandonar.setEnabled(true);
        btn_apostar.setEnabled(true);

        btn_apostar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_pasar.setEnabled(false);
                btn_abandonar.setEnabled(false);
                btn_apostar.setEnabled(false);
                turn_ended = true;
                not_turn = true;
                endOfTurn();
            }
        });
    }

    public void roundManager() {

    }

    public void betting(){


    }
    public void conigureViews(){

        seat1 = findViewById(R.id.SEAT_1_NAME);
        seat2 = findViewById(R.id.SEAT_2_NAME);
        seat3 = findViewById(R.id.SEAT_3_NAME);
        seat4 = findViewById(R.id.SEAT_4_NAME);
        seat5 = findViewById(R.id.SEAT_5_NAME);
        seat6 = findViewById(R.id.SEAT_6_NAME);
        seat7 = findViewById(R.id.SEAT_7_NAME);
        seat8 = findViewById(R.id.SEAT_8_NAME);
        seatsViews.add(seat1);
        seatsViews.add(seat2);
        seatsViews.add(seat3);
        seatsViews.add(seat4);
        seatsViews.add(seat5);
        seatsViews.add(seat6);
        seatsViews.add(seat7);
        seatsViews.add(seat8);
        chips1 = findViewById(R.id.SEAT1_CHIPS);
        chips2 = findViewById(R.id.SEAT2_CHIPS);
        chips3 = findViewById(R.id.SEAT3_CHIPS);
        chips4 = findViewById(R.id.SEAT4_CHIPS);
        chips5 = findViewById(R.id.SEAT5_CHIPS);
        chips6 = findViewById(R.id.SEAT6_CHIPS);
        chips7 = findViewById(R.id.SEAT7_CHIPS);
        chips8 = findViewById(R.id.SEAT8_CHIPS);
        chipsView.add(chips1);
        chipsView.add(chips2);
        chipsView.add(chips3);
        chipsView.add(chips4);
        chipsView.add(chips5);
        chipsView.add(chips6);
        chipsView.add(chips7);
        chipsView.add(chips8);
        btn_apostar = findViewById(R.id.btn_apostar);
        btn_abandonar = findViewById(R.id.btn_abandonar);
        btn_pasar = findViewById(R.id.btn_pasar);
        btn_pasar.setEnabled(false);
        btn_abandonar.setEnabled(false);
        btn_apostar.setEnabled(false);
        mybet = findViewById(R.id.poker_chips_bet);
        seatPlayers();
    }





}
