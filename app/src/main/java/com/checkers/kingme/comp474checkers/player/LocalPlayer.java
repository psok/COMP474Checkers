package com.checkers.kingme.comp474checkers.player;

import android.app.Activity;

import com.checkers.kingme.comp474checkers.backend.Color;
import com.checkers.kingme.comp474checkers.model.GameState;

/**
 * Created by Richa on 2/21/2015.
 */
public class LocalPlayer extends Player
{
    public LocalPlayer(Color color, String name){
        super(color, name);
    }

    public void wake(Activity system) {
        // nothing
    }

    public void notify(int from, char type, int to) {
        // nothing
    }

    public void onTap(int squareId)
    {
        stateHandler.onGetInput(squareId);
    }
}
