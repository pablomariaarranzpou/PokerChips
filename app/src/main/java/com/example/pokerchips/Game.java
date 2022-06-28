package com.example.pokerchips;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import model.Player;

public class Game extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private String userID, roomID;
    private TextView seat1, seat2, seat3, seat4, seat5, seat6, seat7, seat8;
    private TextView chips1, chips2, chips3, chips4, chips5, chips6, chips7, chips8;
    private ArrayList<Player> playersList;
    private ArrayList<TextView> seatsViews;
    private ArrayList<TextView> chipsView;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        seatsViews = new ArrayList<TextView>();
        chipsView = new ArrayList<TextView>();

        if (getIntent().hasExtra("roomID")){
            this.roomID = getIntent().getExtras().getString("roomID");
            Log.d("ROOM", roomID);
        }
        conigureViews();
        seatPlayers();

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
            }
        });
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
    }



}
