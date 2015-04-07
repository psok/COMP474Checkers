package com.checkers.kingme.comp474checkers;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        }

        // Call game activity
        public void sendMessage(View view) {
            Button btnLocalMultiPlayer = (Button) findViewById(R.id.btn_HumanLocal);
            Button btnRemoteMultiPlayer = (Button) findViewById(R.id.btn_HumanRemote);
            if(btnLocalMultiPlayer.isPressed()) {
                Intent intent = new Intent(this, LocalMultiPlayerActivity.class);
                startActivity(intent);
            }else if(btnRemoteMultiPlayer.isPressed()){
                Intent intent = new Intent(this, RemoteMultiPlayerActivity.class);
                startActivity(intent);

            }
        }
}