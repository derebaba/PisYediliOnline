package com.pisyedilionline.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class GenericCard extends BaseActor {

    public GenericCard(Sprite sprite)
    {
        super(sprite);

        this.sprite = sprite;

        setSize(14, 21);

        setTouchable(Touchable.enabled);
    }
}
