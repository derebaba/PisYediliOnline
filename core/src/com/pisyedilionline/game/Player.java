package com.pisyedilionline.game;

import com.pisyedilionline.message.PlayerMessage;

public class Player
{
    private int cardCount = 0;
    private String username = "";
    private int direction;

    public Player(PlayerMessage message)
    {
        this.username = message.getUsername();
        this.cardCount = message.getCardCount();
        this.direction = message.getDirection();
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
