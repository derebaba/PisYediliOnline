package com.pisyedilionline.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Card extends BaseCard implements Comparable<Card> {

    public enum Suit {
		CLUBS, DIAMONDS, HEARTS, SPADES,
    }

    private Suit suit;
    private boolean selected;
    private int value, order;

    public Card(Sprite sprite, Suit suit, int value, int order) {
        super(sprite);

        selected = false;
        this.suit = suit;
        this.value = value;
        this.order = order;

    }

    public int getValue() {
        return value;
    }

    public Suit getSuit() {
        return suit;
    }

    public boolean isSelected() { return selected; }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

	public int getOrder()
	{
		return order;
	}

	@Override
    public int compareTo(Card c)
    {
        return this.order - c.order;
    }
}
