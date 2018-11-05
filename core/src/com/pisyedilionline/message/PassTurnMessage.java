package com.pisyedilionline.message;

public class PassTurnMessage
{
	private int direction;

	private int pile7Count;

	private int jiletSuit;

	private boolean lastCardA;

	public int getDirection()
	{
		return direction;
	}

	public int getPile7Count() {
		return pile7Count;
	}

	public int getJiletSuit() {
		return jiletSuit;
	}

	public boolean isLastCardA() {
		return lastCardA;
	}
}
