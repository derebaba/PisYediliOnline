package com.pisyedilionline.message;

public class DrawCardBroadcastMessage {

    private int direction;
    private int cardCount;
    private int deckSize;

    public int getDirection() {
        return direction;
    }

    public int getCardCount() {
        return cardCount;
    }

    public int getDeckSize() {
        return deckSize;
    }
}
