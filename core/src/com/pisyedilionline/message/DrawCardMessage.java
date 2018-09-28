package com.pisyedilionline.message;

public class DrawCardMessage
{
	private int card;

	public int getCard()
	{
		return card;
	}

	@Override
	public String toString()
	{
		return "Card = " + card;
	}
}
