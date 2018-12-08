package com.pisyedilionline.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool;
import com.pisyedilionline.game.PisYediliOnline;

public class BaseCardPool extends Pool<BaseCard> {

    private Sprite sprite;

    // constructor with initial object count and max object count
    // max is the maximum of object held in the pool and not the
    // maximum amount of objects that can be created by the pool
    public BaseCardPool(final Sprite sprite, int init, int max){
        super(init,max);
        this.sprite = sprite;
    }

    // make pool with default 16 initial objects and no max
    public BaseCardPool(final Sprite sprite){
        super();
        this.sprite = sprite;
    }

    @Override
    protected BaseCard newObject() {
        return new BaseCard(new Sprite(sprite));
    }
}
