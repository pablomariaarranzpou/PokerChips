package com.example.pokerchips;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    Button btn_createRoom, btn_joinRoom;
    TextInputEditText user_name, room_code;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firestore = FirebaseFirestore.getInstance();
        btn_createRoom = findViewById(R.id.create_menu);
        btn_joinRoom = findViewById(R.id.join_menu);
        user_name = findViewById(R.id.edituser);
        room_code = findViewById(R.id.editroom);
        btn_createRoom.setOnClickListener(v-> startActivity(new Intent(MainActivity.this, CreateRoom.class)));

        btn_joinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = user_name.getText().toString().trim();
                String room = room_code.getText().toString().trim();
                if(user.isEmpty()){
                    user_name.setError("Escriba un username");
                    user_name.requestFocus();
                    return;
                }else if(room.isEmpty()){
                    room_code.setError("Escriba una sala");
                    room_code.requestFocus();
                    return;
                }else{
                    DocumentReference doc = firestore.collection("Room").document(room);


                    doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    int inPlayers = Math.toIntExact(document.getLong("inPlayers"));
                                    ++inPlayers;
                                    if(inPlayers > Math.toIntExact(document.getLong("numPlayers"))){
                                        room_code.setError("SALA LLENA");
                                        room_code.requestFocus();
                                        return;
                                    } doc.update("inPlayers", inPlayers).addOnSuccessListener((OnSuccessListener<? super Void>) task1 -> {
                                        Intent in = new Intent(MainActivity.this, WaitingRoom.class);
                                        in.putExtra("roomID", doc.getId());
                                        Map<String, Object> user_new = new HashMap<>();
                                        user_new.put("player_name", user);
                                        user_new.put("player_chips",Math.toIntExact(document.getLong("numChips")));
                                        doc.collection("Player").add(user_new).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                startActivity(in);
                                                finish();
                                            }
                                        });
                                    });

                                } else {
                                    room_code.setError("NO EXISTE");
                                    room_code.requestFocus();
                                    return;
                                }
                            } else {
                                room_code.setError("NO EXISTE");
                                room_code.requestFocus();
                                return;
                            }
                        }
                    });

                }
            }
        });
    }
}