package com.pisyedilionline.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

public class Player
{
    private int cardCount = 0;
    private String username = "";
    private int direction;

    public Player(PlayerMessage message)
    {
        this.username = message.username;
        this.cardCount = message.cardCount;
        this.direction = message.direction;
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
