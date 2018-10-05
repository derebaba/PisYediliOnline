package com.pisyedilionline.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.pisyedilionline.actor.Card;
import org.jetbrains.annotations.NotNull;

public class AllCards {

    private final PisYediliOnline game;
    private Array<Card> cardDeck;
    private Texture cardSheet;

    public AllCards(@NotNull final PisYediliOnline game)
    {
        this.game = game;

        //  Prepare all cards in the deck
        cardSheet = game.assetManager.get("deck.png", Texture.class);
        cardDeck = loadCards();
    }

    public Card getCardById(int id)
    {
        return cardDeck.get(id);
    }

    public Array<Card> getCardDeck() {
        return cardDeck;
    }

	/**
	 * Load card sprites from image. Assign card values and suits.
	 * @return	Card deck in an array
	 */
	private Array<Card> loadCards() {
        TextureRegion region;
        Sprite cardSprite;

        int width = 79;
        int height = 123;

        int xPos = 0;
        int yPos = 0;

        Array<Card> deck = new Array<Card>();

        for (int i = 0; i < 52; i++, xPos += width)
        {
            region = new TextureRegion(cardSheet, xPos, yPos, width, height);
            cardSprite = new Sprite(region);

            //Keeps track of which card in the suit is being loaded
            int value = i % 13;

            //Keeps track of the suit of the card being loaded
            int suitKey = i / 13;

            Card.Suit suit;

            if (suitKey < 1) {
                suit = Card.Suit.CLUBS;
            } else if (suitKey == 1) {
                suit = Card.Suit.DIAMONDS;
            } else if (suitKey == 2) {
                suit = Card.Suit.HEARTS;
            } else {
                suit = Card.Suit.SPADES;
            }

            deck.add(new Card(cardSprite, suit, value,suitKey * 13 + value));

            //Moves to the next column of the sprite sheet after a suit is completed
            if (xPos == width * 12) {
                xPos = -width;
                yPos += height;
            }
        }
        return deck;

    }
}
