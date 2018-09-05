package com.pisyedilionline.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class Player extends Actor
{
    private Array<Card> hand;
    private Array<Sprite> sprites;

    public Player()
    {
        hand = new Array<Card>();
    }

    public void drawCard(Card c)
    {
        hand.add(c);
    }

    @Override
    public void draw (Batch batch, float parentAlpha)
    {
        /*
        for ()
        sprite.draw(batch);*/
    }
}
