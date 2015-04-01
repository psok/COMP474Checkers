package com.checkers.kingme.comp474checkers.frontend;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.GridLayout;
import android.widget.TextView;

import com.checkers.kingme.comp474checkers.backend.Color;
import com.checkers.kingme.comp474checkers.model.CheckersStateMachine;
import com.checkers.kingme.comp474checkers.LocalMultiPlayerActivity;
import com.checkers.kingme.comp474checkers.backend.Piece;
import com.checkers.kingme.comp474checkers.player.LocalPlayer;
import com.checkers.kingme.comp474checkers.player.Player;
import com.checkers.kingme.comp474checkers.R;
import com.checkers.kingme.comp474checkers.model.GameState;
import com.checkers.kingme.comp474checkers.model.ViewUpdateListener;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    { // creates multiple views of squares
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String firstPlayer =  intent.getStringExtra(LocalMultiPlayerActivity.EXTRA_PLAYER1);
        String secondPlayer = intent.getStringExtra(LocalMultiPlayerActivity.EXTRA_PLAYER2);
        setContentView(R.layout.activity_game);

        if(firstPlayer.isEmpty()) {
            firstPlayer = "Player1";
        }
        if(secondPlayer.isEmpty()) {
            secondPlayer = "Player2";
        }

        TextView t1 = (TextView) findViewById(R.id.txt_Player1);
        t1.setText("  " + firstPlayer);
        TextView t2 = (TextView) findViewById(R.id.txt_Player2);
        t2.setText("  " + secondPlayer);

        stateMachine = new CheckersStateMachine(this);

        players = new HashMap<Color, Player>();

        players.put(Color.BLACK, new LocalPlayer(Color.BLACK, firstPlayer, stateMachine));
        players.put(Color.RED, new LocalPlayer(Color.RED, firstPlayer, stateMachine));

        stateMachine.start();
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
                //set index from 0 to 63 to each square
                //tView.index = index++;
                //tView.setOnTapListener(th
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

    /*
    @Override
    public void OnToggled(SquareView v, boolean touchOn) {
        int squareId = v.squareID;
        if (squareId > 0) {
            CurrentBoard currentBoard = stateOfGame.getBoard();
            //Piece piece = currentBoard.getPiece(squareId);

            if(stateOfGame.pickUp(squareId) &&
                    ((v.isBlackPiece && stateOfGame.getTurn() == Color.BLACK) ||
                    (v.isRedPiece && stateOfGame.getTurn() == Color.RED))) {
                v.isHighlighted = !v.isHighlighted;
                previousCall = stateOfGame.pickUp(squareId);
            }
            else if(previousCall && stateOfGame.moveTo(squareId)) {
                currentBoard = stateOfGame.getBoard();
                updateSquareView(v, currentBoard, squareId);
                TextView txt = (TextView) findViewById(R.id.your_turn_text);
                if (stateOfGame.whoseTurn() == null) {
                    txt.setText(txt.getText().subSequence(11, txt.getText().length()) + " WINS!");
                } else {
                    txt.setText("Your turn: " + stateOfGame.getTurn());
                }

                previousCall = false;
            }

            updateBoard(currentBoard);
        }
    }
    */

    // Update isKing, isBlackPiece, isRedPiece of square view in accordance with currentBoard[squareId]
    /*
    private void updateSquareView (SquareView v, Piece[] board) {
        int squareId = v.getSquareID();
        if (squareId > 0 && board[squareId] != null) {

            if (board[squareId].getColor() == Color.BLACK)
            v.isKing = (piece.getRank() == Rank.KING);
            v.isBlackPiece = (piece.getColor() == Color.BLACK);
            v.isRedPiece = (piece.getColor() == Color.RED);
        }
    }
    */

    // Update only used squares on the board after any moves
    public void invalidateView(Piece[] board)
    {
        for (Map.Entry<Integer, SquareView> sqr : wayBack.entrySet()) {
            sqr.getValue().updateSquare(board);
            sqr.getValue().invalidate();
        }
        previouslyHighlighted = 0;
    }

    public void changeTurn(Color turn) {
        TextView txt = (TextView) findViewById(R.id.your_turn_text);
        txt.setText("Your turn: " + turn);
        for (Map.Entry<Integer, SquareView> sqr : wayBack.entrySet()) {
            sqr.getValue().setOnTapListener(players.get(turn));
        }
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

}
