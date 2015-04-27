package com.checkers.kingme.comp474checkers.model;

import com.checkers.kingme.comp474checkers.backend.CheckersGame;
import com.checkers.kingme.comp474checkers.backend.Color;
import com.checkers.kingme.comp474checkers.backend.GameMode;
import com.checkers.kingme.comp474checkers.backend.Piece;

/**
 * Created by Carlos
 */
public class CheckersStateMachine implements GameListener, GameState {
    private CheckersGame game;
    private ViewUpdateListener view;
    private GameMode gameMode;

    GameState currentState;

    private GameState Turn = new GameState() {
        public void onGetInput(int squareID) {
            game.pickUp(squareID);
        }
    };

    private GameState Move = new GameState() {
        public void onGetInput(int squareID) { // a is unnecessary, but if you remove it java doesn't work because it sucks astronomically
            boolean a = game.moveTo(squareID) || game.pickUp(squareID);
        }
    };

    public CheckersStateMachine(ViewUpdateListener system) {
        view = system;
    }

    public CheckersStateMachine(ViewUpdateListener system, GameMode gameMode) {
        view = system;
        this.gameMode = gameMode;
    }

    public void start(NetworkUpdateObserver nuo) {
        if (gameMode == GameMode.ONE_PLAYER) {
            game = new CheckersGame(this, gameMode);
        } else {
            game = new CheckersGame(this);
        }
        game.setNetworkUpdateObserver(nuo);
    }

    public void onBegin(Piece[] board) {
        currentState = Turn;
        view.initialize();
        view.invalidateView(board);
    }

    public void onPick(int from) {
        currentState = Move;
        view.highlight(from);
    }

    public void onMove(Piece[] board) {
        view.invalidateView(board);
    }

    public void onDefeat(Color turn) {
        if (turn == Color.BLACK) {
            view.win(Color.RED);
        } else {
            view.win(Color.BLACK);
        }
    }

    public void onNewTurn(Color turn) {
        if (view.changeTurn(turn)) {
            currentState = Turn;
        }
    }

    public void onGetInput(int squareID) {
        currentState.onGetInput(squareID);
    }

}
