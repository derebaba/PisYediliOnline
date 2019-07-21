package com.pisyedilionline.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.pisyedilionline.message.PlayerMessage;
import com.pisyedilionline.screen.GameScreen;
import org.jetbrains.annotations.NotNull;

public class Player extends Group
{
	protected final GameScreen screen;

    private String name = "";
    private Label nameLabel;
    protected Group cards;
    protected int direction;

    public Player(final GameScreen screen, @NotNull PlayerMessage message)
    {
    	this.screen = screen;
        this.name = message.getUsername();
        this.direction = message.getDirection();

        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = screen.game.font;
        labelStyle.fontColor = Color.GOLD;

        nameLabel = new Label(name, labelStyle);
        addActor(nameLabel);
        nameLabel.setPosition(-10, 10);

        cards = new Group() {
            @Override
            protected void childrenChanged() {
                int childrenCount = getChildren().size;

                for (int i = 0; i < childrenCount; i++)
                {
                    BaseCard card = (BaseCard) getChildren().get(i);
                    card.setZIndex(i);
                    card.setPosition(5 * (i - childrenCount / 2), 0);
                    card.setBounds(card.getX(), card.getY(),
                            card.getSprite().getWidth(), card.getSprite().getHeight());
                }

                nameLabel.setZIndex(childrenCount);
            }
        };
        addActor(cards);

		setTransform(true);
    }

    public String getUsername() {
        return name;
    }

    public void setUsername(String username) {
        this.name = username;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public Group getCards() {
        return cards;
    }
}
