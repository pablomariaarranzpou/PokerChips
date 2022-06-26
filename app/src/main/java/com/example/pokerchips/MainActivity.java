package com.example.pokerchips;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    Button btn_createRoom, btn_joinRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_createRoom = findViewById(R.id.create_menu);
        btn_createRoom.setOnClickListener(v-> startActivity(new Intent(MainActivity.this, CreateRoom.class)));
    }
}