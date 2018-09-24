package com.pisyedilionline.message;

public class PlayerMessage
{
    private String username;
	private int cardCount;
	private int direction;

    @Override
    public String toString() {
        return "username: " + username + " cardCount = " + cardCount + " direction = " + direction;
    }

	public String getUsername()
	{
		return username;
	}

	public int getCardCount()
	{
		return cardCount;
	}

	public int getDirection()
	{
		return direction;
	}
}
