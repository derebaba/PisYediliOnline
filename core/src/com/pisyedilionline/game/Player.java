package com.pisyedilionline.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

public class Player extends BaseActor
{
    private Array<Card> hand;


    public Player(Sprite sprite)
    {
        super(sprite);

        hand = new Array<Card>();
    }

    public void drawCard(Card card)
    {
        hand.add(card);
    }

    public Array<Card> getHand()
    {
        return hand;
    }
}
