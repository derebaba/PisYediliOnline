package com.pisyedilionline.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class GameScreen extends BaseScreen {

	final Card.Suit[] SUITS = Card.Suit.values();

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

    public void setHand(int[] hand)
    {

    }

    private void dealHands()
    {
        for (int i = 0; i < 7; i++)
        {
            //opponent.drawCard(cardDeck.pop());
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
            int value = (i % 13) + 1;

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

            deck.add(new Card(cardSprite, suit, value, this,suitKey * 15 + value));

            //Moves to the next column of the sprite sheet after a suit is completed
            if (xPos == width * 12) {
				xPos = -width;
                yPos += height;
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
        //game.font.draw(game.batch, Integer.toString(opponent.getHand().size), opponent.getX() + 20, opponent.getY() + 10);
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
