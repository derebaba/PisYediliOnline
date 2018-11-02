package com.pisyedilionline.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.pisyedilionline.message.PlayerMessage;
import com.pisyedilionline.screen.GameScreen;
import org.jetbrains.annotations.NotNull;

public class Player extends Opponent {

    public Player(final GameScreen screen, @NotNull PlayerMessage message)
    {
        super(screen, message);
    }

	public void addCard(Card card)
	{
		if (!hasChildren())
		{
			addActor(card);
			return;
		}

		for (int i = 0; i < getChildren().size - 1; i++)
		{
			Card currentCard = (Card) getChildren().get(i);
			Card nextCard = (Card) getChildren().get(i + 1);

			if (card.getOrder() > currentCard.getOrder())
			{
				if (card.getOrder() < nextCard.getOrder())
				{
					addActorAfter(currentCard, card);
					return;
				}
			}
			else
			{
				addActorBefore(currentCard, card);
				return;
			}
		}

		addActorAt(getChildren().size, card);
	}

    public void playCard(int cardId)
	{
		for (Actor actor : getChildren())
		{
			Card card = (Card) actor;
			if (card.getOrder() == cardId)
			{
				removeActor(card);
				break;
			}
		}
		screen.playCard(this, cardId);
	}

	public void playCard(Card card)
	{
		removeActor(card);
		screen.playCard(this, card.getOrder());
	}

	public void enableHand(InputListener cardListener, boolean isFirstHand, Card topCard)
	{
		if (isFirstHand)
		{
			for (Actor actor : getChildren())
			{
				Card card = (Card) actor;
				//	first round: player can only play clubs
				if (card.getSuit() == Card.Suit.CLUBS)
				{
					card.addListener(cardListener);
				}
				else
				{
					card.getSprite().setColor(Color.LIGHT_GRAY);
				}
			}
		}
		else
		{
			//	not first round
			for (Actor actor : getChildren())
			{
				Card card = (Card) actor;
				if (card.getSuit() == topCard.getSuit() || card.getValue() == topCard.getValue() ||
						card.getValue() == 10)
				{
					//	same suit or same value or jilet
					card.addListener(cardListener);
				}
				else
				{
					card.getSprite().setColor(Color.LIGHT_GRAY);
				}
			}
		}
	}

	public void disableHand()
	{
		for (Actor actor : getChildren())
		{
			Card card = (Card) actor;
			card.getSprite().setColor(Color.WHITE);
			card.clearListeners();
		}
	}
}
