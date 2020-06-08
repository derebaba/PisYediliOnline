package com.pisyedilionline.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.pisyedilionline.screen.GameScreen;

public class Pile extends Group {
    protected final GameScreen screen;
    public int size;

    public Pile(final GameScreen screen)
    {
        this.screen = screen;
        size = 0;
    }

    public Card peek()
    {
        return (Card) getChildren().peek();
    }

    @Override
    public void addActor(Actor actor)
    {
        super.addActor(actor);

        Card card = (Card) actor;
        card.setBounds(card.getX(), card.getY(),
                card.getSprite().getWidth(), card.getSprite().getHeight());
        size++;
    }

}
