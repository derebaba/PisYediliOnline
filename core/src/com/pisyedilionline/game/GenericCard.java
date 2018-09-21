package com.pisyedilionline.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class GenericCard extends BaseActor {

    protected final GameScreen screen;

    public GenericCard(Sprite sprite, final GameScreen screen)
    {
        super(sprite);

        this.sprite = sprite;
        this.screen = screen;

        setSize(14, 21);

        setTouchable(Touchable.enabled);
    }
}
