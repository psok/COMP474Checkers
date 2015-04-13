package com.checkers.kingme.comp474checkers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.checkers.kingme.comp474checkers.frontend.CheckersSystem;


public class ComputerPlayerActivity extends ActionBarActivity {


    private String player1="";
    public final static String EXTRA_PLAYER1 = "player1Name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oneplayer);
    }
    // Call game activity
    public void sendMessage(View view) {

        Intent intent = new Intent(this , CheckersSystem.class);

        //Send  player's name to checkersSystem activity
        EditText editTextFirstPlayer = (EditText) findViewById(R.id.edittext_onePlayerHuman);
        setPlayer1(editTextFirstPlayer.getText().toString());
        intent.putExtra(EXTRA_PLAYER1, player1);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_one_player, menu);
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

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }
}
