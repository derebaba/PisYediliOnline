package com.pisyedilionline.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Card extends GenericCard implements Comparable<Card> {

    public enum Suit {
        HEARTS, SPADES, DIAMONDS, CLUBS
    }

    private Suit cSuit;
    private boolean selected;
    private int value, order;

    //Constructor for regular card
    public Card(Sprite sprite, Suit suit, int value, final GameScreen screen, int order) {
        super(sprite, screen);

        selected = false;
        this.cSuit = suit;
        this.value = value;
        this.order = order;
    }

    public int getValue() {
        return value;
    }

    public Suit getSuit() {
        return cSuit;
    }

    public boolean isSelected() { return selected; }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int compareTo(Card c)
    {
        return c.order - this.order;
    }
}
