package com.pisyedilionline.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.pisyedilionline.message.PlayerMessage;
import com.pisyedilionline.screen.GameScreen;
import org.jetbrains.annotations.NotNull;

public class MainPlayer extends Player {

    public int drawnRegularCardCount = 0;
    public int drawn7Count = 0;
    public boolean lastCardADrawn = false;

    public MainPlayer(final GameScreen screen, @NotNull PlayerMessage message)
    {
        super(screen, message);
    }

    public void addCard(Card card)
    {
        if (!cards.hasChildren())
        {
            cards.addActor(card);
            return;
        }

        for (int i = 0; i < cards.getChildren().size - 1; i++)
        {
            Card currentCard = (Card) cards.getChildren().get(i);
            Card nextCard = (Card) cards.getChildren().get(i + 1);

            if (card.getOrder() > currentCard.getOrder())
            {
                if (card.getOrder() < nextCard.getOrder())
                {
                    cards.addActorAfter(currentCard, card);
                    return;
                }
            }
            else
            {
                cards.addActorBefore(currentCard, card);
                return;
            }
        }

        cards.addActorAt(cards.getChildren().size, card);
    }

    /**
     *
     * @return true if user can play a card
     */
    public boolean enableHand(InputListener cardListener, boolean isFirstHand, int pile7count, int jiletSuit, Card topCard)
    {
        boolean canPlayCard = false;
        for (Actor actor : cards.getChildren())
        {
            Card card = (Card) actor;
            card.getSprite().setColor(Color.LIGHT_GRAY);

            //	first round: player can only play clubs
            if (isFirstHand){   // first hand rules
                if(card.getSuit() == Card.Suit.CLUBS){
                    card.getSprite().setColor(Color.WHITE);
                    card.addListener(cardListener);
                    canPlayCard = true;
                }
            }
            else if (pile7count > 0){   // 7 played rules
                if(drawn7Count == 0 && card.getValue() == topCard.getValue()){
                    card.getSprite().setColor(Color.WHITE);
                    card.addListener(cardListener);
                    canPlayCard = true;
                }
                else if (drawn7Count == pile7count * GameScreen.DRAW_CARD_COUNT_PER_7){
                    if (card.getSuit() == topCard.getSuit()
                            || card.getValue() == topCard.getValue()
                            || card.getValue() == 10){ // regular card rules
                        card.getSprite().setColor(Color.WHITE);
                        card.addListener(cardListener);
                        canPlayCard = true;
                    }
                }
            }
            else if (jiletSuit != -1) {	//	jilet is played
                if (card.getOrder() / 13 == jiletSuit)
                {
                    card.getSprite().setColor(Color.WHITE);
                    card.addListener(cardListener);
                    canPlayCard = true;
                }
            }
            else if (card.getSuit() == topCard.getSuit()
                    || card.getValue() == topCard.getValue() ||
                    card.getValue() == 10){ // regular card rules
                card.getSprite().setColor(Color.WHITE);
                card.addListener(cardListener);
                canPlayCard = true;
            }
        }
        return canPlayCard;
    }

    public void disableHand()
    {
        for (Actor actor : cards.getChildren())
        {
            Card card = (Card) actor;
            card.getSprite().setColor(Color.LIGHT_GRAY);
            card.clearListeners();
        }
    }
}
