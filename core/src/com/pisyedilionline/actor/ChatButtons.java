package com.pisyedilionline.actor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.gson.Gson;
import com.pisyedilionline.game.PisYediliOnline;
import com.pisyedilionline.message.ChatMessage;
import com.pisyedilionline.message.Opcode;
import com.pisyedilionline.message.PlayCardMessage;
import com.pisyedilionline.screen.GameScreen;

public class ChatButtons extends Group {
    private final PisYediliOnline game;
    private final GameScreen gameScreen;
    private final Label title;

    public ChatButtons(final PisYediliOnline game, final GameScreen gameScreen)
    {
        this.game = game;
        this.gameScreen = gameScreen;

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = game.font;
        labelStyle.fontColor = Color.WHITE;

        title = new Label("Quick chat", labelStyle);
        title.setPosition(0, 0);
        title.setFontScale(0.5f);
        title.setSize(getWidth(), 10);
        title.setTouchable(Touchable.disabled);
        addActor(title);
    }

    public void addButton(int messageCode, String message)
    {
        TextButton button = new TextButton(message, game.skin);
        button.getLabel().setFontScale(0.2f);
        button.setX(getX());
        button.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                ChatMessage message = new ChatMessage(messageCode, gameScreen.getMainPlayer().getUsername());
                game.nakama.getSocket().sendMatchData(game.matchId, Opcode.CHAT_SEND.id, new Gson().toJson(message).getBytes());
                return true;
            }
        });
        addActorAt(0, button);

        for (int i = 0; i < getChildren().size; i++)
        {
            Actor child = getChild(i);
            button.setSize(getWidth(), 10);
            child.setY(10 * i);
        }
        title.setPosition(0, (getChildren().size - 1) * 10);
    }
}
