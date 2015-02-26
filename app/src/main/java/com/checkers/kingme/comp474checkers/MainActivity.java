package com.checkers.kingme.comp474checkers;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends ActionBarActivity
    {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        }

        // Call game activity
        public void sendMessage(View view) {
            Intent intent = new Intent(this, Game.class);
            startActivity(intent);
        }
        // Set the visibility of the start button
        public void startVisibility(View view) {
            ImageButton btnStart= (ImageButton) findViewById(R.id.startButton);
            btnStart.setVisibility(view.VISIBLE);
        }
}