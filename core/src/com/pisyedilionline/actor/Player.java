package com.pisyedilionline.actor;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.pisyedilionline.message.PlayerMessage;
import com.pisyedilionline.screen.GameScreen;
import org.jetbrains.annotations.NotNull;

public class Player extends Group
{
	protected final GameScreen screen;

    private String username = "";
    protected int direction;

    public Player(final GameScreen screen, @NotNull PlayerMessage message)
    {
    	this.screen = screen;
        this.username = message.getUsername();
        this.direction = message.getDirection();

		setTransform(true);
    }

    @Override
    protected void childrenChanged()
	{
	    int childrenCount = getChildren().size;

		for (int i = 0; i < childrenCount; i++)
		{
			BaseCard card = (BaseCard) getChildren().get(i);
			card.setZIndex(i);
			card.setPosition(5 * (i - childrenCount / 2), 0);
			card.setBounds(card.getX(), card.getY(),
					card.getSprite().getWidth(), card.getSprite().getHeight());
		}
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
