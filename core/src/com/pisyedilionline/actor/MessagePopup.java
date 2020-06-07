package com.pisyedilionline.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.pisyedilionline.screen.GameScreen;

public class MessagePopup extends Actor {

    private final GameScreen screen;
    private String message;

    public MessagePopup(final GameScreen screen, String message)
    {
        this.screen = screen;
        this.message = message;
    }


}
