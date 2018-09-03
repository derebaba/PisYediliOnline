package com.pisyedilionline.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class CardStack extends Actor {

    private Sprite sprite;
    private Texture texture;

    public CardStack()
    {
        texture = new Texture(Gdx.files.internal("regularBlue.jpg"));
        sprite = new Sprite(texture);
        sprite.setSize(17, 24);

        setTouchable(Touchable.enabled);
    }

    public Texture getTexture() { return texture; }
    public Sprite getSprite() { return sprite; }
}
