package com.pisyedilionline.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class GameScreen extends BaseScreen {

    private Texture cardSheet;

    private Array<Card> cardDeck;

    public GameScreen(final PisYediliOnline game) {
        super(game);

        loadCards();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
/*
        for (int i = 0; i < 10; i++)
        {
            cardDeck.get(i).getSprite().setPosition(20 * i, 0);
            cardDeck.get(i).getSprite().setSize(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f);
            cardDeck.get(i).getSprite().draw(game.batch);
        }
*/
        cardDeck.get(0).getSprite().setPosition(0, 0);
        cardDeck.get(0).getSprite().setSize(WORLD_WIDTH, WORLD_HEIGHT);
        cardDeck.get(0).getSprite().draw(game.batch);

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
    public void dispose() {
        cardSheet.dispose();
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
