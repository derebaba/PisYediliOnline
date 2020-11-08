package com.pisyedilionline.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.pisyedilionline.game.PisYediliOnline;
import com.pisyedilionline.screen.GameScreen;

public class ChatMessagePopup extends Group {

    private final GameScreen screen;
    private Label label;
    protected Image bubbleImage;

    public ChatMessagePopup(final PisYediliOnline game, final GameScreen screen, String message)
    {
        bubbleImage = new Image(game.assetManager.get("speech_bubble.png", Texture.class));
        addActor(bubbleImage);

        label = new Label(message, new Label.LabelStyle(game.font, Color.BLACK));
        label.setSize(240, 160);
        //label.setPosition(20, 60);
        label.setWrap(true);
        label.setFontScale(1f);
        label.setAlignment(Align.center);
        addActor(label);

        this.screen = screen;

        bubbleImage.setSize(240, 160);

        setTouchable(Touchable.disabled);
    }

}
