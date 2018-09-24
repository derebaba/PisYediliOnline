package com.pisyedilionline.game;

import com.badlogic.gdx.utils.Array;
import com.pisyedilionline.message.GameStartMessage;
import com.pisyedilionline.message.PlayerMessage;
import com.pisyedilionline.screen.GameScreen;

import java.util.ArrayList;

public class GameState {

    private ArrayList<Integer> hand;
    private Array<Player> opponents;
    private int deckSize = 0;
    private ArrayList<Integer> pile;
    private int direction;
    private int turn;
    private String username;
    private GameScreen gameScreen;

    public GameState(GameStartMessage message, String username)
    {
        hand = new ArrayList<Integer>();
        for (int id : message.getCards()) {
            hand.add(id);
        }

        opponents = new Array<Player>();
        for (int i = 0; i < message.getPlayers().length; i++)
        {
            PlayerMessage player = message.getPlayers()[i];
            if (player.getUsername().equals(username))
            {
                direction = player.getDirection();
            }
            else
            {
                opponents.add(new Player(player));
            }
        }

        this.deckSize = message.getDeckSize();

        pile = new ArrayList<Integer>();

        this.turn = message.getTurn();

        this.username = username;
    }

    public int getDeckSize() {
        return deckSize;
    }

    public void setDeckSize(int deckSize) {
        this.deckSize = deckSize;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Integer> getHand() {
        return hand;
    }

    public Array<Player> getOpponents() {
        return opponents;
    }

    public ArrayList<Integer> getPile() {
        return pile;
    }
}
