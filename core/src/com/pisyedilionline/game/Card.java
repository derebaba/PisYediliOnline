package com.pisyedilionline.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

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
        cSprite.setSize(14, 19);

        this.cSuit = suit;
        this.value = value;

        setTouchable(Touchable.enabled);
        addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println(cSuit + " " + getValue());
                //System.out.printf("%f, %f\n", x, y);
                event.handle();//the Stage will stop trying to handle this event
                return true; //the inputmultiplexer will stop trying to handle this touch
            }
        });
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
        cSprite.draw(batch);
    }

    @Override
    protected void positionChanged() {
        cSprite.setPosition(getX(),getY());
        super.positionChanged();
    }
}
