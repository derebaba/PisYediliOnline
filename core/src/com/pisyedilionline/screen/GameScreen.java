package com.pisyedilionline.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Array;
import com.pisyedilionline.actor.BaseCard;
import com.pisyedilionline.game.AllCards;
import com.pisyedilionline.actor.Card;
import com.pisyedilionline.actor.Opponent;
import com.pisyedilionline.game.PisYediliOnline;
import com.pisyedilionline.message.GameStartMessage;
import com.pisyedilionline.message.Opcode;
import com.pisyedilionline.message.PlayerMessage;

import java.util.ArrayList;

public class GameScreen extends BaseScreen
{
	final Card.Suit[] SUITS = Card.Suit.values();

	//	GAME VARIABLES
	private Array<Opponent> opponents;
	private int deckSize = 0;
	private int direction;	//	this player's chair position
	private int turn;	//	whose turn is it

	/**
	 * how many turns have passed (starts from 1 because lua)
	 */
	private int turnCount = 0;
	private boolean clockwise = true;
	private String username, matchId;

	//	CARDS
	private AllCards allCards;
	private Array<Card> hand, pile;
	private Array<BaseCard> pool;
	private BaseCard deck;

	//	TODO: turn yuvarlağı yerine Opponent'a bi indicator ekle
	private float turnX = 0, turnY = 0;	//	turnün kimde olduğunu gösteren yuvarlağın pozisyonu

    public GameScreen(final PisYediliOnline game, GameStartMessage message, String username, String matchId) {
        super(game);

        //	add deck to stage
		allCards = new AllCards(game);
		for (Card card : allCards.getCardDeck())
		{
			stage.addActor(card);
		}

		pile = new Array<Card>();

		//	Create pool of upside-down cards for opponent hands
		pool = new Array<>(52);
		for (int i = 0; i < 52; i++)
		{
			BaseCard poolCard = new BaseCard(new Sprite(game.assetManager.get("regularBlue.jpg", Texture.class)));
			pool.add(poolCard);
			stage.addActor(poolCard);
		}

		//	initialize hand
		hand = new Array<Card>();
		for (int id : message.getCards()) {
			Card card = allCards.getCardById(id);
			hand.add(card);
		}

		//	initialize opponents
		opponents = new Array<Opponent>();
		for (int i = 0; i < message.getPlayers().length; i++)
		{
			PlayerMessage playerMessage = message.getPlayers()[i];
			if (playerMessage.getUsername().equals(username))
			{
				direction = playerMessage.getDirection();
				game.logger.info("Direction: " + direction);
			}
			else
			{
				Opponent opponent = new Opponent(game, playerMessage);

				for (int j = 0; j < playerMessage.getCardCount(); j++)
				{
					opponent.addCard(pool.pop());
				}

				opponents.add(opponent);
				stage.addActor(opponent);
			}
		}

		//	set position of opponents
		if (opponents.size == 1)
		{
			opponents.get(0).setPosition(30, 65);
		}
		else
		{
			for (Opponent opponent : opponents)
			{
				if (opponent.getDirection() == (direction + 1) % message.getPlayers().length)
				{
					opponent.setPosition(0, 80);
					opponent.rotateBy(-90);
				}
				else if (opponent.getDirection() == (direction + 2) % message.getPlayers().length)
				{
					opponent.setPosition(30, 69);
				}
				else if (opponent.getDirection() == (direction + 3) % message.getPlayers().length)
				{
					opponent.setPosition(160, 10);
					opponent.rotateBy(90);
				}
			}
		}

		this.deckSize = message.getDeckSize();
		this.clockwise = message.isClockwise();
		this.turn = message.getTurn();
		this.matchId = matchId;
		this.username = username;


        //  Prepare card deck to draw
		deck = new BaseCard(new Sprite(game.assetManager.get("regularBlue.jpg", Texture.class)));
        stage.addActor(deck);
        deck.setPosition(55, 40);

        update();

		if (turn == direction)
		{
			enableDeck();
		}
    }

    public void update()
    {
        sortCards();

        for (Card card: hand)
		{
			if (turnCount < opponents.size + 1)
			{
				//	first round
				if (card.getSuit() == Card.Suit.CLUBS)
				{
					card.getSprite().setColor(Color.YELLOW);
					card.addListener(new InputListener()
					{
						@Override
						public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
						{
							playCard((Card) event.getTarget());
							return true;
						}
					});
				}
			}
			else
			{
				//	not first round
				Card topCard = pile.peek();
			}
		}

		if (turn == direction)
		{
			turnX = 80;
			turnY = 5;
		}
		else
		{
			for (Opponent opponent : opponents)
			{
				if (turn == opponent.getDirection())
				{
					turnX = opponent.getX();
					turnY = opponent.getY();
				}
			}
		}
    }

    public void drawCard(int cardId)
    {
        hand.add(allCards.getCardById(cardId));
        sortCards();
        enableDeck();
    }

	//	PRIVATE METHODS
	private void playCard(Card card)
	{
		hand.removeValue(card, false);
		card.setPosition(80, 40);
		card.setZIndex(100 + turnCount++);   //  100 is arbitrary
		card.clearListeners();
		sortCards();
	}

	private void enableDeck()
	{
		deck.addListener(new InputListener()
		{
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

				BaseCard animationCard = pool.peek();
				animationCard.setPosition(deck.getX(), deck.getY());

				animationCard.addAction(Actions.sequence(
						Actions.moveTo(80, 0, 0.5f), Actions.moveTo(1000, 1000)));

				game.logger.info("Sending draw card message");

				game.nakama.getSocket().sendMatchData(matchId, Opcode.DRAW_CARD.id, new byte[0]);

				//	clear listeners until server sends the card
				deck.clearListeners();

				event.handle();
				return true;
			}
		});
	}

	/**
	 * Sort and position cards in the hand
	 */
	private void sortCards()
	{
		hand.sort();
		for (int i = 0; i < hand.size; i++)
		{
			Card card = hand.get(i);
			card.setZIndex(i);
			card.setPosition(30 + 10 * i, 5);
			card.setBounds(card.getX(), card.getY(),
					card.getSprite().getWidth(), card.getSprite().getHeight());
		}
	}

	//	SCREEN OVERRIDE METHODS
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

		game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		game.shapeRenderer.setColor(1, 0, 0, 1);

		game.shapeRenderer.circle(turnX, turnY, 5);

		game.shapeRenderer.end();
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
    }


}
