package com.pisyedilionline.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.pisyedilionline.game.PisYediliOnline;
import com.pisyedilionline.message.PlayerMessage;
import com.pisyedilionline.screen.GameScreen;
import org.jetbrains.annotations.NotNull;

public class Opponent extends Actor
{
	private GameScreen screen;

    private int cardCount = 0;
    private String username = "";
    private int direction;
    private Array<BaseCard> cards;

    public Opponent(GameScreen screen, @NotNull PlayerMessage message)
    {
    	this.screen = screen;
        this.username = message.getUsername();
        this.cardCount = message.getCardCount();
        this.direction = message.getDirection();

		cards = new Array<BaseCard>();

		positionCards();
    }

    private void positionCards()
	{
		for (int i = 0; i < cardCount; i++)
		{
			BaseCard card = screen.getPool().pop();
			cards.add(card);
			card.setZIndex(i);
			card.setPosition(20 + 10 * i, 70);
			card.setBounds(card.getX(), card.getY(),
					card.getSprite().getWidth(), card.getSprite().getHeight());
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

    @Override
	public void draw(Batch batch, float parentAlpha)
	{
		for (BaseCard card : cards)
		{
			card.draw(batch, parentAlpha);
		}
	}
}
