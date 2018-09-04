package com.pisyedilionline.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GenericCard extends Actor {

    private Sprite sprite;
    protected final GameScreen screen;

    public GenericCard(Sprite sprite, final GameScreen screen)
    {
        this.sprite = sprite;
        this.screen = screen;

        setSize(14, 19);

        setTouchable(Touchable.enabled);
    }

    public Sprite getSprite() { return sprite; }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        sprite.draw(batch);
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        sprite.setPosition(getX(),getY());
    }

    @Override
    protected void sizeChanged()
    {
        super.sizeChanged();
        sprite.setSize(getWidth(), getHeight());
    }
}
