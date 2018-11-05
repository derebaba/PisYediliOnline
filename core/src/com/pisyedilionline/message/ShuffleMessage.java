package com.pisyedilionline.message;

public class ShuffleMessage {

    private int deckSize;
    private int topCard;
    private int topSuit;

    @Override
    public String toString() {
        return "deckSize: " + deckSize + " topCard = " + topCard;
    }

    public int getDeckSize() {
        return deckSize;
    }

    public int getTopCard() {
        return topCard;
    }

    public int getTopSuit() {
        return topSuit;
    }
}
