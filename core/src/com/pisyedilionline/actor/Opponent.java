package com.pisyedilionline.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.pisyedilionline.game.PisYediliOnline;
import com.pisyedilionline.message.PlayerMessage;
import com.pisyedilionline.screen.GameScreen;
import org.jetbrains.annotations.NotNull;

public class Opponent extends Group
{
	private final PisYediliOnline game;

    private String username = "";
    private int direction;

    public Opponent(final PisYediliOnline game, @NotNull PlayerMessage message)
    {
    	this.game = game;
        this.username = message.getUsername();
        this.direction = message.getDirection();

		setTransform(true);
    }

    private void positionCards()
	{
	    int childrenCount = getChildren().size;

		for (int i = 0; i < childrenCount; i++)
		{
			Actor card = getChildren().get(i);
			card.setZIndex(i);
			card.setPosition(5 * (i - childrenCount), 0);
		}
	}

	public void addCard(BaseCard card)
	{
		addActor(card);
		positionCards();
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
