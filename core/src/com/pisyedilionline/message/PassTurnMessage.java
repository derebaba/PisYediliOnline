package com.pisyedilionline.message;

public class PassTurnMessage
{
	private int direction;

	private int mustDraw;

	public int getDirection()
	{
		return direction;
	}

	/**
	 * How many cards does the player have to draw
	 * @return	number of cards to draw
	 */
	public int getMustDraw()
	{
		return mustDraw;
	}
}
