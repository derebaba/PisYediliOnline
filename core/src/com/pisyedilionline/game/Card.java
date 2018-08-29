package com.pisyedilionline.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Card extends Actor {

    public enum Suit {
        HEARTS, SPADES, DIAMONDS, CLUBS
    }

    private Sprite cSprite;
    private Suit cSuit;

    private int value;

    //Constructor for regular card
    public Card(Sprite sprite, Suit suit, int value) {
        this.cSprite = sprite;
        this.cSuit = suit;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Sprite getSprite() {
        return cSprite;
    }

    public Suit getSuit() {
        return cSuit;
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(cSprite, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

}
