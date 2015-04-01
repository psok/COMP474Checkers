package com.checkers.kingme.comp474checkers.player;

import com.checkers.kingme.comp474checkers.backend.Color;
import com.checkers.kingme.comp474checkers.frontend.SquareView;
import com.checkers.kingme.comp474checkers.model.GameState;

/**
 * Created by JESSIE on 3/27/15.
 */
public abstract class Player implements SquareView.OnTapListener
{
    protected Color color;
    protected String name;
    protected GameState state;

    public Player(Color color, String name, GameState state) {
        this.color = color;
        this.name = name;
        this.state = state;
    }

    public String getName()
    {
        return this.name;
    }
}
