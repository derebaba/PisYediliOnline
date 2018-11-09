package com.pisyedilionline.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;

public class BaseCard extends Actor implements Pool.Poolable
{
    protected Sprite sprite;

    public BaseCard(Sprite sprite)
    {
        this.sprite = sprite;

        setSize(14, 21);
        setPosition(-100,-100);

        sprite.setColor(Color.LIGHT_GRAY);
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

    @Override
    public void reset() {
        setPosition(-100,-100);
    }
}
