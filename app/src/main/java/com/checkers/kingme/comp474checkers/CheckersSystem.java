package com.checkers.kingme.comp474checkers;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import com.checkers.kingme.comp474checkers.SquareView.OnToggledListener;

import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.GridLayout;
import android.widget.TextView;

public class CheckersSystem extends ActionBarActivity
        implements OnToggledListener{

    private CheckersGame stateOfGame;
    private int fromSquare = -1;
    private int toSquare = -1;
    private int previousIndex = 0;
    private boolean isPieceSelected = false;
    SquareView[] squareViews;
    GridLayout myGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // creates multiple views of squares
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        myGridLayout = (GridLayout)findViewById(R.id.checkersGrid); // creates legal checkerboard
        int tempCount = 0;
        boolean everyTwo = false;
        boolean everyEight = true;
        int numOfCol = myGridLayout.getColumnCount();
        int numOfRow = myGridLayout.getRowCount();
        squareViews = new SquareView[numOfCol*numOfRow];
        stateOfGame = new CheckersGame();
        int index = 0;
        for(int yPos=0; yPos<numOfRow; yPos++){
            for(int xPos=0; xPos<numOfCol; xPos++){
                int squareId=-1;
                if(everyEight) {
                    if (everyTwo && everyEight) {
                        tempCount++;
                        squareId = tempCount;
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
                }
                else{
                    everyEight = true;
                }
                SquareView tView = new SquareView(this, xPos, yPos, squareId);
                //set index from 0 to 63 to each square
                tView.index = index++;
                if (squareId > 0) {
                    CurrentBoard currentBoard = stateOfGame.getBoard();
                    updateSquareView(tView, currentBoard, squareId);
                }
                tView.setOnToggledListener(this);
                squareViews[yPos * numOfCol + xPos] = tView;
                myGridLayout.addView(tView);
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

    @Override
    public void OnToggled(SquareView v, boolean touchOn) {
        int squareId = v.squareID;
        if (squareId > 0) {
            CurrentBoard currentBoard = stateOfGame.getBoard();
            Piece piece = currentBoard.getPiece(squareId);
            isPieceSelected = !isPieceSelected;
            if (isPieceSelected) {
                if (piece == null) {
                    isPieceSelected = !isPieceSelected;
                } else {
                    setSquare(squareId);
                }
            } else {
                setSquare(squareId);
            }

            if (fromSquare > 0 && toSquare > 0 && !isPieceSelected) {
                if (stateOfGame.pickUp(this.fromSquare) && stateOfGame.moveTo(this.toSquare)) {
                    currentBoard = stateOfGame.getBoard();
                    updateSquareView(v, currentBoard, toSquare);
                    TextView txt = (TextView) findViewById(R.id.your_turn_text);
                    txt.setText("Your turn: " + stateOfGame.getTurn());
                }
            } else {
                previousIndex = v.index;
            }
            updateBoard(currentBoard);
        }
    }

    // Update isKing, isBlackPiece, isRedPiece of square view in accordance with currentBoard[squareId]
    private void updateSquareView (SquareView v, CurrentBoard currentBoard, int squareId) {
        /*v.isKing = currentBoard.isKing(squareId);
        v.isBlackPiece = currentBoard.isBlackPiece(squareId);
        v.isRedPiece = currentBoard.isRedPiece(squareId);*/
        Piece piece = currentBoard.getPiece(squareId);
        if(piece != null) {
            v.isKing = (piece.getRank() == Rank.KING);
            v.isBlackPiece = (piece.getColor() == Color.BLACK);
            v.isRedPiece = (piece.getColor() == Color.RED);
        }
        else {
            v.isKing = false;
            v.isBlackPiece = false;
            v.isRedPiece = false;
        }
    }

    // Update only used squares on the board after any moves
    private void updateBoard(CurrentBoard currentBoard) {
        int index = 0;
        int squareID = 1;
        for(int yPos=0; yPos<8; yPos++){
            for(int xPos=0; xPos<8; xPos++) {
                if(yPos % 2 == 0) {
                    if(xPos % 2 == 1) {
                        updateSquareView(squareViews[index], currentBoard, squareID);
                        squareViews[index].invalidate();
                        squareID ++;
                    }
                }
                else {
                    if(xPos % 2 == 0) {
                        updateSquareView(squareViews[index], currentBoard, squareID);
                        squareViews[index].invalidate();
                        squareID ++;
                    }
                }
                index ++;
            }
        }

    }

    /**
     * @return  the current board
     */
    public CurrentBoard getTheBoard() {
        return stateOfGame.getBoard();
    }

    public void setSquare(int toSquare) {
        this.fromSquare = this.toSquare;
        this.toSquare = toSquare;
        //isPieceSelected = !isPieceSelected;
    }

}
