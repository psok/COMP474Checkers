package com.checkers.kingme.comp474checkers.model;

import com.checkers.kingme.comp474checkers.backend.CheckersGame;
import com.checkers.kingme.comp474checkers.backend.Color;
import com.checkers.kingme.comp474checkers.backend.Piece;
import com.checkers.kingme.comp474checkers.player.Player;

/**
 * Created by Carlos
 */
public class CheckersStateMachine implements GameListener, GameState
{
    private CheckersGame game;
    private ViewUpdateListener view;

    GameState currentState;

    private GameState Turn = new GameState() {
        public void onGetInput (int squareID)
        {
            game.pickUp(squareID);
        }
    };

    private GameState Move = new GameState() {
        public void onGetInput (int squareID)
        { // a is unnecessary, but if you remove it java doesn't work because it sucks astronomically
            boolean a = game.moveTo(squareID) || game.pickUp(squareID);
        }
    };

    public CheckersStateMachine(ViewUpdateListener system)
    {
        view = system;
    }

    public void start()
    {
        game = new CheckersGame(this);
    }

    public void onBegin(Piece[] board)
    {
        currentState = Turn;
        view.initialize();
        view.invalidateView(board);
    }

    public void onPick(int from)
    {
        currentState = Move;
        view.highlight(from);
    }

    public void onMove(Piece[] board)
    {
        view.invalidateView(board);
    }

    public void onDefeat(Color turn)
    {
        if (turn == Color.BLACK) {
            view.win(Color.RED);
        } else {
            view.win(Color.BLACK);
        }
    }

    public void onNewTurn(Color turn)
    {
        currentState = Turn;
        view.changeTurn(turn);
    }

    public void onGetInput(int squareID)
    {
        currentState.onGetInput(squareID);
    }

}
