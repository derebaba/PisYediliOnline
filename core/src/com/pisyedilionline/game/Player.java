package com.pisyedilionline.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

public class Player extends BaseActor
{
    public enum Direction
    {
        NORTH, EAST, SOUTH, WEST
    }

    private int cardCount = 0;
    private String name = "";
    private Direction direction;

    public Player(Sprite sprite)
    {
        super(sprite);
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
