package com.pisyedilionline.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.pisyedilionline.message.PlayerMessage;
import com.pisyedilionline.screen.GameScreen;
import org.jetbrains.annotations.NotNull;

public class Opponent extends Group
{
	private GameScreen screen;

    private int cardCount = 0;
    private String username = "";
    private int direction;

    public Opponent(GameScreen screen, @NotNull PlayerMessage message)
    {
    	this.screen = screen;
        this.username = message.getUsername();
        this.cardCount = message.getCardCount();
        this.direction = message.getDirection();

		setTransform(true);

		for (int i = 0; i < cardCount; i++)
		{
			BaseCard card = screen.getPool().pop();
			addActor(card);
		}

		positionCards();
    }

    private void positionCards()
	{
		for (int i = 0; i < getChildren().size; i++)
		{
			Actor card = getChildren().get(i);
			card.setZIndex(i);
			card.setPosition(10 * i, 0);
		}
	}

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
