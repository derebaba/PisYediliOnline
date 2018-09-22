package com.pisyedilionline.game;

import com.badlogic.gdx.utils.Array;

public class GameState {

    private Array<Card> hand;
    private Array<Player> opponents;
    private int deckSize = 0;
    private Card topCard;
    private Player.Direction direction;

    public GameState()
    {
        hand = new Array<Card>();
        opponents = new Array<Player>();
    }

    public int getDeckSize() {
        return deckSize;
    }

    public void setDeckSize(int deckSize) {
        this.deckSize = deckSize;
    }

    public Card getTopCard() {
        return topCard;
    }

    public void setTopCard(Card topCard) {
        this.topCard = topCard;
    }

    public Array<Card> getHand() {
        return hand;
    }

    public Array<Player> getOpponents() {
        return opponents;
    }

    public Player.Direction getDirection() {
        return direction;
    }

    public void setDirection(Player.Direction direction) {
        this.direction = direction;
    }
}
