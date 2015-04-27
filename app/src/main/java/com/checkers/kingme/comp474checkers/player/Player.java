package com.checkers.kingme.comp474checkers.player;

import android.app.Activity;

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
    protected GameState stateHandler;

    public Player(Color color, String name) {
        this.color = color;
        this.name = name;
    }

    public void setStateHandler(GameState state)
    {
        this.stateHandler = state;
    }

    public String getName()
    {
        return this.name;
    }

    abstract public void wake(Activity system);

    abstract public void notify(int from, char type, int to);
}
