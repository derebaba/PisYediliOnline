package com.pisyedilionline.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.pisyedilionline.actor.BaseCard;
import com.pisyedilionline.game.AllCards;
import com.pisyedilionline.actor.Card;
import com.pisyedilionline.actor.Opponent;
import com.pisyedilionline.game.PisYediliOnline;
import com.pisyedilionline.message.GameStartMessage;
import com.pisyedilionline.message.PlayerMessage;

import java.util.ArrayList;

public class GameScreen extends BaseScreen
{
	final Card.Suit[] SUITS = Card.Suit.values();

	private Array<Opponent> opponents;
	private int deckSize = 0;
	private int direction;
	private int turn;
	private String username;

	private AllCards allCards;
	private Array<Card> hand;
	private Array<Card> pile;
	private Array<BaseCard> pool;

    public GameScreen(final PisYediliOnline game, GameStartMessage message, String username) {
        super(game);

		allCards = new AllCards(game);
		pile = new Array<Card>();
		pool = new Array<>(52);
		for (int i = 0; i < 52; i++)
		{
			pool.add(new BaseCard(new Sprite(game.assetManager.get("regularBlue.jpg", Texture.class))));
		}

		hand = new Array<Card>();
		for (int id : message.getCards()) {
			Card card = allCards.getCardById(id);
			hand.add(card);
			stage.addActor(card);
		}

		opponents = new Array<Opponent>();
		for (int i = 0; i < message.getPlayers().length; i++)
		{
			PlayerMessage playerMessage = message.getPlayers()[i];
			if (playerMessage.getUsername().equals(username))
			{
				direction = playerMessage.getDirection();
			}
			else
			{
				Opponent opponent = new Opponent(this, playerMessage);
				opponents.add(opponent);
				stage.addActor(opponent);
			}
		}

		//	TODO: sil
		opponents.get(0).setPosition(80, 70);

		this.deckSize = message.getDeckSize();

		this.turn = message.getTurn();

		this.username = username;


        //  Prepare card stack to draw
        Sprite stackSprite = new Sprite(game.assetManager.get("regularBlue.jpg", Texture.class));
        /*
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
        opponent = new Opponent(opponentSprite);
        stage.addActor(opponent);
        opponent.setPosition(70, 70);
        dealHands();
*/
        update();
    }

    public void update()
    {
    	/*
        for (int id : game.state.getHand())
        {
        	Card card = allCards.getCardById(id);
            hand.add(allCards.getCardById(id));
			stage.addActor(card);
        }
        */
        sortCards();
    }

	public Array<BaseCard> getPool()
	{
		return pool;
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
        game.font.draw(game.batch, Integer.toString(deckSize),62, 38);
        //game.font.draw(game.batch, Integer.toString(opponent.getHand().size), opponent.getX() + 20, opponent.getY() + 10);
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
        game.assetManager.dispose();
    }


}
