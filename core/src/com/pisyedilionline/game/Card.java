package com.pisyedilionline.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Card extends GenericCard implements Comparable<Card> {

    public enum Suit {
		CLUBS, DIAMONDS, HEARTS, SPADES,
    }

    private Suit cSuit;
    private boolean selected;
    private int value, order;

    //Constructor for regular card
    public Card(Sprite sprite, Suit suit, int value, int order) {
        super(sprite);

        selected = false;
        this.cSuit = suit;
        this.value = value;
        this.order = order;

        addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                /*screen.playCard((Card) event.getTarget());

                screen.game.logger.info("Played card: " + suit + " " + value);
*/
                event.handle();//the Stage will stop trying to handle this event
                return true; //the inputmultiplexer will stop trying to handle this touch
            }
        });
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
        return this.order - c.order;
    }
}
