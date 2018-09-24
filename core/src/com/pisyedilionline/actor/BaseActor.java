package com.pisyedilionline.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class BaseActor extends Actor
{
    protected Sprite sprite;

    protected BaseActor(Sprite sprite)
    {
        this.sprite = sprite;

        setSize(14, 19);
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
