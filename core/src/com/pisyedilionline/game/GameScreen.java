package com.pisyedilionline.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class GameScreen extends BaseScreen {

	final Card.Suit[] SUITS = Card.Suit.values();

	private AllCards allCards;
	private Array<Card> hand;
    private int turn = 0;

    public GameScreen(final PisYediliOnline game) {
        super(game);

        allCards = new AllCards(game);
        hand = new Array<Card>();

        for (Card card : allCards.getCardDeck())
        {
            stage.addActor(card);
        }
        //  Prepare card stack to draw
        Sprite stackSprite = new Sprite(game.assetManager.get("regularBlue.jpg", Texture.class));
        /*
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
*/
        Sprite opponentSprite = new Sprite(game.assetManager.get("regularBlue.jpg", Texture.class));
        /*
        opponent = new Player(opponentSprite);
        stage.addActor(opponent);
        opponent.setPosition(70, 70);
        dealHands();
*/
        update();
    }

    public void update()
    {
        for (int id : game.state.getHand())
        {
            hand.add(allCards.getCardById(id));
        }
        sortCards();
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
    /*
    public void drawCard()
    {
        Card card = cardDeck.pop();
        stage.addActor(card);
        hand.add(card);
        sortCards();
    }
*/
    public void playCard(Card card)
    {
        hand.removeValue(card, false);
        card.setPosition(80, 40);
        card.setZIndex(100 + turn++);   //  100 is arbitrary
        card.clearListeners();
        sortCards();
    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        super.render(delta);

        game.batch.begin();
        game.font.draw(game.batch, Integer.toString(game.state.getDeckSize()),62, 38);
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
