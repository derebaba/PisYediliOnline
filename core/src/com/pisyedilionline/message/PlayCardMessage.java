package com.pisyedilionline.message;

public class PlayCardMessage
{
	private int cardId, playerDirection, jiletSuit = -1;

	public PlayCardMessage(int cardId, int playerDirection, int jiletSuit) {
		this.cardId = cardId;
		this.playerDirection = playerDirection;
		this.jiletSuit = jiletSuit;
	}

	public int getCardId()
	{
		return cardId;
	}

	public int getPlayerDirection()
	{
		return playerDirection;
	}

	public int getJiletSuit() {
		return jiletSuit;
	}
}
