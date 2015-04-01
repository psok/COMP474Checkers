package com.checkers.kingme.comp474checkers.player;

import com.checkers.kingme.comp474checkers.backend.Color;
import com.checkers.kingme.comp474checkers.model.GameState;

/**
 * Created by Richa on 2/21/2015.
 */
public class LocalPlayer extends Player {

    public LocalPlayer(Color color, String name, GameState state){
        super(color, name, state);
    }

    public void onTap(int squareId)
    {
        state.onGetInput(squareId);
    }
}
