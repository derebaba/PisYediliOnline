package com.pisyedilionline.message;

public class PassTurnMessage
{
	private int direction;

	private int pile7Count;

	private boolean lastCardA;

	private int turnCount;

	public int getDirection()
	{
		return direction;
	}

	public int getPile7Count() {
		return pile7Count;
	}

	public boolean isLastCardA() {
		return lastCardA;
	}

	public int getTurnCount() {
		return turnCount;
	}
}
