package com.pisyedilionline.message;

import com.pisyedilionline.message.PlayerMessage;

import java.util.Arrays;

public class GameStartMessage {

    private int[] cards;
	private PlayerMessage[] players;
	private int turn, deckSize;

    @Override
    public String toString() {
        return "Cards: " + Arrays.toString(cards) + " turn = " + turn + " deckSize: " + deckSize + " Players: " + Arrays.deepToString(players);
    }

	public int[] getCards()
	{
		return cards;
	}

	public PlayerMessage[] getPlayers()
	{
		return players;
	}

	public int getTurn()
	{
		return turn;
	}

	public int getDeckSize()
	{
		return deckSize;
	}
}
