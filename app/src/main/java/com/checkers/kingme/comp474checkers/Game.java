package com.checkers.kingme.comp474checkers;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import com.checkers.kingme.comp474checkers.MyView.OnToggledListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.GridLayout;
import android.widget.Toast;

public class Game extends ActionBarActivity
        implements OnToggledListener{

    int numberOfGlobalLayoutCalled = 0;

    MyView[] myViews;

    GridLayout myGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        myGridLayout = (GridLayout)findViewById(R.id.mygrid);
        int tempCount = 0;
        boolean everyTwo = false;
        boolean everyEight = true;
        int numOfCol = myGridLayout.getColumnCount();
        int numOfRow = myGridLayout.getRowCount();
        myViews = new MyView[numOfCol*numOfRow];
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

                MyView tView = new MyView(this, xPos, yPos, squareId);
                tView.isBlackPiece = (squareId >= 1 && squareId <= 12);
                tView.isRedPiece = (squareId >= 21 && squareId <= 32);
                tView.setOnToggledListener(this);
                myViews[yPos*numOfCol + xPos] = tView;
                myGridLayout.addView(tView);
            }
        }

        myGridLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener(){

                    @Override
                    public void onGlobalLayout() {

                        int pLength;
                        final int MARGIN = 5;

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

                        int w = pLength/numOfCol; //pWidth/numOfCol;
                        int h = pLength/numOfRow; //pHeight/numOfRow;

                        for(int yPos=0; yPos<numOfRow; yPos++){
                            for(int xPos=0; xPos<numOfCol; xPos++){
                                GridLayout.LayoutParams params =
                                        (GridLayout.LayoutParams)myViews[yPos*numOfCol + xPos].getLayoutParams();
                                params.width = w/* - 2*MARGIN*/;
                                params.height = h/* - 2*MARGIN*/;
                                //params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
                                myViews[yPos*numOfCol + xPos].setLayoutParams(params);
                            }
                        }

                        Toast.makeText(Game.this,
                                "numberOfGlobalLayoutCalled = " + numberOfGlobalLayoutCalled,
                                Toast.LENGTH_SHORT).show();
                        numberOfGlobalLayoutCalled++;

                        //deprecated in API level 16
                        myGridLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        //for API Level >= 16
                        //myGridLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }});
    }

    @Override
    public void OnToggled(MyView v, boolean touchOn) {

        //get the id string
        String idString = v.getIdX() + ":" + v.getIdY() + "\nSquareID: " + v.getSquareID();

        Toast.makeText(Game.this,
                "Toggled:\n" +
                        idString + "\n" +
                        touchOn,
                Toast.LENGTH_SHORT).show();
    }

}
