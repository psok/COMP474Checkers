package com.checkers.kingme.comp474checkers.player;

import android.app.Activity;

import com.checkers.kingme.comp474checkers.backend.Color;
import com.checkers.kingme.comp474checkers.model.GameState;

/**
 * Created by Richa on 2/21/2015.
 */
public class ComputerPlayer extends Player
{
    public ComputerPlayer(Color color, String name) {
        super(color, name);
    }

    public void onTap(int squareId) {
        // nothing
    }

    public void wake(Activity system) {
        // nothing
    }

    public void notify(int from, char type, int to) {
        // nothing
    }
}
