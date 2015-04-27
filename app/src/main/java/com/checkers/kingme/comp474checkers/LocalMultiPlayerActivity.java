package com.checkers.kingme.comp474checkers;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.checkers.kingme.comp474checkers.backend.Color;
import com.checkers.kingme.comp474checkers.frontend.CheckersSystem;
import com.checkers.kingme.comp474checkers.player.LocalPlayer;


public class LocalMultiPlayerActivity extends ActionBarActivity
{
    /*
    private String player1="Player1";
    private String player2="Player2";
    public final static String EXTRA_PLAYER1 = "Player1";
    public final static String EXTRA_PLAYER2 = "Player2";
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
    }
    // Call game activity
    public void sendMessage(View view) {
        String blackName;
        String redName;
        Intent intent = new Intent(this, CheckersSystem.class);

        //Send  player's name to checkersSystem activity
        EditText editTextFirstPlayer = (EditText) findViewById(R.id.edittext_firstplayer);

        if (editTextFirstPlayer.getText().toString().isEmpty()) {
            blackName = "Player1";
        } else {
            blackName = editTextFirstPlayer.getText().toString();
        }
        EditText editTextSecondPlayer= (EditText) findViewById(R.id.editText_secondPlayer);

        if (editTextSecondPlayer.getText().toString().isEmpty()) {
            redName = "Player2";
        } else {
            redName = editTextSecondPlayer.getText().toString();
        }

        MainActivity.blackPlayer = new LocalPlayer(Color.BLACK, blackName);
        MainActivity.redPlayer = new LocalPlayer(Color.RED, redName);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_local_multi_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
