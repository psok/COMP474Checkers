package com.checkers.kingme.comp474checkers.frontend;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.GridLayout;
import android.widget.TextView;

import com.checkers.kingme.comp474checkers.ComputerPlayerActivity;
import com.checkers.kingme.comp474checkers.LocalMultiPlayerActivity;
import com.checkers.kingme.comp474checkers.MainActivity;
import com.checkers.kingme.comp474checkers.R;
import com.checkers.kingme.comp474checkers.backend.Color;
import com.checkers.kingme.comp474checkers.backend.GameMode;
import com.checkers.kingme.comp474checkers.backend.Piece;
import com.checkers.kingme.comp474checkers.model.CheckersStateMachine;
import com.checkers.kingme.comp474checkers.model.NetworkUpdateObserver;
import com.checkers.kingme.comp474checkers.model.ViewUpdateListener;
import com.checkers.kingme.comp474checkers.player.LocalPlayer;
import com.checkers.kingme.comp474checkers.player.Player;

import java.util.HashMap;
import java.util.Map;

public class CheckersSystem extends ActionBarActivity
    implements ViewUpdateListener
{
    CheckersStateMachine stateMachine;
    SquareView[] squareViews;
    GridLayout myGridLayout;

    Map<Integer, SquareView> wayBack;
    Map<Color, Player> players;
    private int previouslyHighlighted = 0;
    public GameMode gameMode;
    public Color currentTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    { // creates multiple views of squares
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String firstPlayer = "";
        String secondPlayer = "";
        setContentView(R.layout.activity_game);

        players = new HashMap<Color, Player>();

        if(intent.getStringExtra(ComputerPlayerActivity.EXTRA_PLAYER1) != null
                && intent.getStringExtra(ComputerPlayerActivity.EXTRA_PLAYER2) != null) {
            firstPlayer = intent.getStringExtra(ComputerPlayerActivity.EXTRA_PLAYER1);
            secondPlayer = intent.getStringExtra(ComputerPlayerActivity.EXTRA_PLAYER2);
            gameMode = GameMode.ONE_PLAYER;
            players.put(Color.BLACK, new LocalPlayer(Color.BLACK, firstPlayer));
            players.put(Color.RED, new LocalPlayer(Color.RED, secondPlayer));
            stateMachine = new CheckersStateMachine(this,gameMode);
        }
        else {
            firstPlayer = MainActivity.blackPlayer.getName();
            secondPlayer = MainActivity.redPlayer.getName();

            players.put(Color.BLACK, MainActivity.blackPlayer);
            players.put(Color.RED, MainActivity.redPlayer);
            stateMachine = new CheckersStateMachine(this);
        }

        TextView t1 = (TextView) findViewById(R.id.txt_Player1);
        t1.setText("  " + firstPlayer);
        TextView t2 = (TextView) findViewById(R.id.txt_Player2);
        t2.setText("  " + secondPlayer);

        players.get(Color.BLACK).setStateHandler(stateMachine);
        players.get(Color.RED).setStateHandler(stateMachine);

        stateMachine.start(new NetworkUpdateObserver() {
            @Override
            public void onCompleteMove(int from, char type, int to) {
                if (currentTurn == Color.BLACK) {
                    players.get(Color.RED).notify(from, type, to);
                } else {
                    players.get(Color.BLACK).notify(from, type, to);
                }
            }
        });
    }

    public void initialize()
    {
        myGridLayout = (GridLayout)findViewById(R.id.checkersGrid); // creates legal checkerboard

        int tempCount = 0;
        boolean everyTwo = false;
        boolean everyEight = true;
        int numOfCol = myGridLayout.getColumnCount();
        int numOfRow = myGridLayout.getRowCount();
        squareViews = new SquareView[numOfCol*numOfRow];
        wayBack = new HashMap<Integer, SquareView>();
        for(int yPos=0; yPos<numOfRow; yPos++){
            for(int xPos=0; xPos<numOfCol; xPos++) {
                int squareId = -1;
                Square square = new LightSquare();
                if (everyEight) {
                    if (everyTwo && everyEight) {
                        tempCount++;
                        squareId = tempCount;
                        square = new DarkSquare();
                        everyTwo = false;
                        if (tempCount % 4 == 0) {
                            everyTwo = true;
                        }
                        if (tempCount % 8 == 0) {
                            everyTwo = false;
                            everyEight = false;
                        }

                    } else {
                        everyTwo = true;
                    }
                } else {
                    everyEight = true;
                }

                SquareView tView = new SquareView(this, square, squareId);
                squareViews[yPos * numOfCol + xPos] = tView;
                myGridLayout.addView(tView);

                if (squareId != -1) {
                    wayBack.put(squareId, tView);
                }
            }
        }

        myGridLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener(){

                    @Override
                    public void onGlobalLayout() { // creates square based on screen resolution

                        int pLength;

                        int pWidth = myGridLayout.getWidth();
                        int pHeight = myGridLayout.getHeight();
                        int numOfCol = myGridLayout.getColumnCount();
                        int numOfRow = myGridLayout.getRowCount();

                        //Set myGridLayout equal width and height
                        if(pWidth>=pHeight){
                            pLength = pHeight;
                        }
                        else{
                            pLength = pWidth;
                        }
                        ViewGroup.LayoutParams pParams = myGridLayout.getLayoutParams();
                        pParams.width = pLength;
                        pParams.height = pLength;
                        myGridLayout.setLayoutParams(pParams);

                        int w = pLength/numOfCol;
                        int h = pLength/numOfRow;

                        for(int yPos=0; yPos<numOfRow; yPos++){
                            for(int xPos=0; xPos<numOfCol; xPos++){
                                GridLayout.LayoutParams params =
                                        (GridLayout.LayoutParams)squareViews[yPos*numOfCol + xPos].getLayoutParams();
                                params.width = w;
                                params.height = h;
                                squareViews[yPos*numOfCol + xPos].setLayoutParams(params);
                            }
                        }
                    }});
    }

    // Update only used squares on the board after any moves
    public void invalidateView(Piece[] board)
    {
        for (Map.Entry<Integer, SquareView> sqr : wayBack.entrySet()) {
            sqr.getValue().updateSquare(board);
            sqr.getValue().invalidate();
        }
        previouslyHighlighted = 0;
    }

    public boolean changeTurn(Color turn) {
        players.get(turn).wake(this);
        if (!turn.equals(currentTurn)) {
            TextView txt = (TextView) findViewById(R.id.your_turn_text);
            txt.setText("Your turn: " + turn);
            currentTurn = turn;
            for (Map.Entry<Integer, SquareView> sqr : wayBack.entrySet()) {
                sqr.getValue().setOnTapListener(players.get(turn));
            }
            return true;
        }
        return false;
    }

    public void win(Color turn) {
        TextView txt = (TextView) findViewById(R.id.your_turn_text);
        txt.setText(turn + " WINS!");
        for (Map.Entry<Integer, SquareView> sqr : wayBack.entrySet()) {
            sqr.getValue().setOnTapListener(null);
        }
    }

    public void highlight(int squareID)
    {
        if (previouslyHighlighted != 0) {
            wayBack.get(previouslyHighlighted).unHighlight();
            wayBack.get(previouslyHighlighted).invalidate();
        }

        wayBack.get(squareID).highlight();
        wayBack.get(squareID).invalidate();
        previouslyHighlighted = squareID;
    }

    //resign button
    public void resign(View view) {
        new AlertDialog.Builder(this)
            .setTitle("Resign Request")
            .setMessage("Are you sure you want to resign from the game?")
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if(currentTurn == Color.BLACK)
                        win(Color.RED);
                    else
                        win(Color.BLACK);
                }
            })
            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

    //backToMenu button
    public void backToMenu(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    //NewGame button
    public void newGame(View view) {
        new AlertDialog.Builder(this)
                .setTitle("New Game Request")
                .setMessage("Are you sure you want to end your current game and start a new game?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), CheckersSystem.class);
                        finish();
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}

