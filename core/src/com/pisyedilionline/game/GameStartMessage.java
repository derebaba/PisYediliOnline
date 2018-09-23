package com.pisyedilionline.game;

import java.util.Arrays;

public class GameStartMessage {

    int[] cards;
    PlayerMessage[] players;
    int turn, deckSize;

    @Override
    public String toString() {
        return "Cards: " + Arrays.toString(cards) + " turn = " + turn + " deckSize: " + deckSize + " Players: " + Arrays.deepToString(players);
    }
}
