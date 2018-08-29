package com.pisyedilionline.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class GameScreen extends BaseScreen {

    private Texture cardSheet, stackTexture;

    private Array<Card> cardDeck;
    private Sprite stack;

    public GameScreen(final PisYediliOnline game) {
        super(game);

        stackTexture = new Texture(Gdx.files.internal("regularBlue.jpg"));
        stack = new Sprite(stackTexture);
        stack.setPosition(43, 41);
        stack.setSize(17, 24);

        loadCards();
        cardDeck.shuffle();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        super.render(delta);

        game.batch.begin();

        for (int i = 0; i < 10; i++)
        {
            cardDeck.get(i).getSprite().setPosition(10 * i, 5);
            cardDeck.get(i).getSprite().setSize(14, 19);
            cardDeck.get(i).getSprite().draw(game.batch);
        }
        stack.draw(game.batch);
        game.batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose()
    {
        cardSheet.dispose();
        stackTexture.dispose();
    }

    private void loadCards() {
        TextureRegion region;
        Sprite cardSprite;

        int row = 9;

        int width = 140;
        int height = 190;

        int xPos = 0;
        int yPos = 0;

        int value = 10;

        int valKey;
        int suitKey;

        Card.Suit suit;

        cardSheet = new Texture(Gdx.files.internal("deck.png"));
        cardDeck = new Array<Card>();

        for (int i = 0; i < 52; i++, yPos += height) {
            if (xPos == 140 && yPos == height * 3) {
                i--;
                continue;
            }

            region = new TextureRegion(cardSheet, xPos, yPos, width, height);
            cardSprite = new Sprite(region);

            //Keeps track of which card in the suit is being loaded
            valKey = i % 13;

            if (valKey >= 0 && valKey < 3) {
                value = 12 + valKey;
            } else if (valKey == 3) {
                value = 11;
            } else {
                value--;
            }

            //Keeps track of the suit of the card being loaded
            suitKey = i / 13;

            if (suitKey < 1) {
                suit = Card.Suit.SPADES;
            } else if (suitKey == 1) {
                suit = Card.Suit.HEARTS;
            } else if (suitKey == 2) {
                suit = Card.Suit.DIAMONDS;
            } else {
                suit = Card.Suit.CLUBS;
            }

            //A special exception for two cards on the spritesheet which are out of place
            if (valKey == 12 && suitKey == 1) {
                suit = Card.Suit.CLUBS;
            } else if (valKey == 12 && suitKey > 2) {
                suit = Card.Suit.HEARTS;
            }

            Card card = new Card(cardSprite, suit, value);
            cardDeck.add(card);


            //Moves to the next column of the sprite sheet after reaching the 10th row
            if (yPos >= height * row) {
                yPos = -height;
                xPos += width;
            }
        }


    }
}
