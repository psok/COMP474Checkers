package com.checkers.kingme.comp474checkers;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.os.Bundle;

public class MainActivity extends ActionBarActivity
    {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        }

        public void sendMessage(View view) {
            Intent intent = new Intent(this, Game.class);
            startActivity(intent);
        }
}