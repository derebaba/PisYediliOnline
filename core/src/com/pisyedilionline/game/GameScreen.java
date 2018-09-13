package com.pisyedilionline.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class GameScreen extends BaseScreen {

    private Array<Card> cardDeck, hand;
    private GenericCard cardStack;
    private Texture cardSheet;
    private int turn = 0;
    Player opponent;

    public GameScreen(final PisYediliOnline game) {
        super(game);

        //  Prepare all cards in the deck
        cardSheet = game.assetManager.get("deck.png", Texture.class);
        cardDeck = loadCards();
        cardDeck.shuffle();

        //  Prepare card stack to draw
        Sprite stackSprite = new Sprite(game.assetManager.get("regularBlue.jpg", Texture.class));
        cardStack = new GenericCard(stackSprite, this);
        stage.addActor(cardStack);
        cardStack.setPosition(60, 40);
        cardStack.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                drawCard();
                event.handle();
                return true;
            }
        });

        hand = new Array<Card>();

        Sprite opponentSprite = new Sprite(game.assetManager.get("regularBlue.jpg", Texture.class));
        opponent = new Player(opponentSprite);
        stage.addActor(opponent);
        opponent.setPosition(70, 70);
        dealHands();

    }

    private void dealHands()
    {
        for (int i = 0; i < 7; i++)
        {
            drawCard();
            opponent.drawCard(cardDeck.pop());
        }
    }

    private void sortCards()
    {
        hand.sort();
        for (int i = 0; i < hand.size; i++)
        {
            Card card = hand.get(i);
            card.setZIndex(i);
            card.setPosition(20 + 10 * i, 5);
            card.setBounds(card.getX(), card.getY(),
                    card.getSprite().getWidth(), card.getSprite().getHeight());
        }
    }

    //  TODO: kendi sırası değilse false dönsün
    public void drawCard()
    {
        Card card = cardDeck.pop();
        stage.addActor(card);
        hand.add(card);
        sortCards();
    }

    public void playCard(Card card)
    {
        hand.removeValue(card, false);
        card.setPosition(80, 40);
        card.setZIndex(100 + turn++);   //  100 is arbitrary
        card.clearListeners();
        sortCards();
    }

    /**
     * Load card sprites from image. Assign card values and suits. Return card deck in an array.
     */
    private Array<Card> loadCards() {
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

        Array<Card> deck = new Array<Card>();

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

            Card card = new Card(cardSprite, suit, value, this,suitKey * 15 + valKey);
            deck.add(card);


            //Moves to the next column of the sprite sheet after reaching the 10th row
            if (yPos >= height * row) {
                yPos = -height;
                xPos += width;
            }
        }
        return deck;

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        super.render(delta);

        game.batch.begin();
        game.font.draw(game.batch, Integer.toString(cardDeck.size),62, 38);
        game.font.draw(game.batch, Integer.toString(opponent.getHand().size), opponent.getX() + 20, opponent.getY() + 10);
        game.batch.end();

        //	sürekli maç oluşturuyor
        //if (game.isConnected()) game.nakama.createMatch();
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
        super.dispose();
        game.assetManager.dispose();
    }


}
