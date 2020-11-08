package com.pisyedilionline.actor;

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
import com.pisyedilionline.message.ChatMessageClient;
import com.pisyedilionline.message.Opcode;
import com.pisyedilionline.screen.GameScreen;

public class ChatButtonGroup extends Group {
    private final PisYediliOnline game;
    private final GameScreen gameScreen;
    private final Label title;

    public ChatButtonGroup(final PisYediliOnline game, final GameScreen gameScreen)
    {
        this.game = game;
        this.gameScreen = gameScreen;

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = game.font;
        labelStyle.fontColor = Color.WHITE;

        title = new Label("Quick chat", labelStyle);
        title.setPosition(0, 0);
        title.setSize(getWidth(), 80);
        title.setTouchable(Touchable.disabled);
        addActor(title);
    }

    public void addButton(int messageCode, String message)
    {
        TextButton button = new TextButton(message, game.skin);
        button.setX(getX());
        button.getLabel().setFontScale(1f);
        button.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                ChatMessageClient message = new ChatMessageClient(messageCode, gameScreen.getMainPlayer().getUsername());
                game.nakama.getSocket().sendMatchData(game.matchId, Opcode.CHAT_SEND.id, new Gson().toJson(message).getBytes());
                return true;
            }
        });
        addActorAt(0, button);

        for (int i = 0; i < getChildren().size; i++)
        {
            Actor child = getChild(i);
            button.setSize(getWidth(), 80);
            child.setY(80 * i);
        }
        title.setPosition(0, (getChildren().size - 1) * 80);
    }
}
