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
    private Texture cardSheet, stackTexture;
    private int turn = 0;

    public GameScreen(final PisYediliOnline game) {
        super(game);

        //  Prepare all cards in the deck
        cardSheet = new Texture(Gdx.files.internal("deck.png"));
        cardDeck = loadCards();
        cardDeck.shuffle();

        //  Prepare card stack to draw
        stackTexture = new Texture(Gdx.files.internal("regularBlue.jpg"));
        Sprite stackSprite = new Sprite(stackTexture);
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
    }

    private void sortCards()
    {
        hand.sort();
        for (int i = 0; i < hand.size; i++)
        {
            hand.get(i).setZIndex(i);
            hand.get(i).setPosition(20 + 10 * i, 5);
            hand.get(i).setBounds(hand.get(i).getX(), hand.get(i).getY(),
                    hand.get(i).getSprite().getWidth(), hand.get(i).getSprite().getHeight());
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
            card.addListener(new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    playCard((Card) event.getTarget());
                    event.handle();//the Stage will stop trying to handle this event
                    return true; //the inputmultiplexer will stop trying to handle this touch
                }
            });
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
        super.dispose();
        cardSheet.dispose();
        stackTexture.dispose();
    }


}
