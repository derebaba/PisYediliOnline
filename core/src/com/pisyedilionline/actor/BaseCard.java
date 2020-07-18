package com.pisyedilionline.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;

public class BaseCard extends Actor implements Pool.Poolable
{
    public static final int HEIGHT = 168, WITDH = 112;
    protected Sprite sprite;

    public BaseCard(Sprite sprite)
    {
        this.sprite = sprite;

        setSize(112, 168);
        reset();
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
        setPosition(-800,-800);
        sprite.setColor(Color.LIGHT_GRAY);
    }

    public void resetColor(){
        sprite.setColor(Color.LIGHT_GRAY);
    }
}
