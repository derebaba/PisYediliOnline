package com.pisyedilionline.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class BaseCard extends Actor
{
    protected Sprite sprite;

    protected BaseCard(Sprite sprite)
    {
        this.sprite = sprite;

        setSize(14, 21);
    }

    public Sprite getSprite() { return sprite; }

    @Override
    public void draw (Batch batch, float parentAlpha)
    {
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
