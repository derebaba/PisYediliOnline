package com.pisyedilionline.game;

import com.badlogic.gdx.utils.Array;

public class Player
{
    private String name;
    private Array<Card> hand;

    public Player(String name)
    {
        this.name = name;
        hand = new Array<Card>();
    }

    public void drawCard(Card c)
    {

    }
}
