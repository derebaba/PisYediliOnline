package com.pisyedilionline.game;

import java.util.Arrays;

public class GameStartMessage {

    class PlayerMessage
    {
        String username;
        int cardCount;
        int direction;

        @Override
        public String toString() {
            return "username: " + username + " cardCount = " + cardCount + " direction = " + direction;
        }
    }

    public int[] cards;
    PlayerMessage[] playerMessages;
    int turn, deckSize;

    @Override
    public String toString() {
        return "Cards: " + Arrays.toString(cards) + " turn = " + turn + " deckSize: " + deckSize + " Players: " + Arrays.deepToString(playerMessages);
    }
}
