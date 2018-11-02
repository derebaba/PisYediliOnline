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
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;
import com.pisyedilionline.actor.BaseCard;
import com.pisyedilionline.actor.Player;
import com.pisyedilionline.game.AllCards;
import com.pisyedilionline.actor.Card;
import com.pisyedilionline.actor.Opponent;
import com.pisyedilionline.game.PisYediliOnline;
import com.pisyedilionline.message.GameStartMessage;
import com.pisyedilionline.message.Opcode;
import com.pisyedilionline.message.PlayerMessage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GameScreen extends BaseScreen
{
	final Card.Suit[] SUITS = Card.Suit.values();

	//	GAME VARIABLES
	private Opponent opponents[];
	private Player player;
	private int deckSize = 0;
	private int turn;	//	whose turn is it
	private int mustDraw = 0;

	/**
	 * how many turns have passed
	 */
	private int turnCount = 0;

	private String matchId;

	//	CARDS
	private AllCards allCards;
	private Array<Card> pile;
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

		//	initialize opponents
		opponents = new Opponent[message.getPlayers().length];
		for (int i = 0; i < message.getPlayers().length; i++)
		{
			PlayerMessage playerMessage = message.getPlayers()[i];
			if (playerMessage.getUsername().equals(username))
			{
				player = new Player(this, playerMessage);

				for (int id : message.getCards()) {
					Card card = allCards.getCardById(id);
					player.addCard(card);
				}

				opponents[player.getDirection()] = player;
				stage.addActor(player);
			}
			else
			{
				Opponent opponent = new Opponent(this, playerMessage);

				for (int j = 0; j < playerMessage.getCardCount(); j++)
				{
					opponent.addActor(pool.pop());
				}

				opponents[opponent.getDirection()] = opponent;
				stage.addActor(opponent);
			}
		}

		player.setPosition(80, 0);
		//	set position of opponents
		if (opponents.length == 2)
		{
			//	if there is only 1 opponent, let him sit across
			for (Opponent opponent : opponents)
			{
				if (opponent.getDirection() != player.getDirection())
				{
					opponent.setPosition(80, 69);
				}
			}
		}
		else
		{
			for (Opponent opponent : opponents)
			{
				if (opponent.getDirection() != player.getDirection())
				{
					if (opponent.getDirection() == (player.getDirection() + 1) % message.getPlayers().length)
					{
						opponent.setPosition(0, 45);
						opponent.rotateBy(-90);
					}
					else if (opponent.getDirection() == (player.getDirection() + 2) % message.getPlayers().length)
					{
						opponent.setPosition(80, 69);
					}
					else if (opponent.getDirection() == (player.getDirection() + 3) % message.getPlayers().length) {
						opponent.setPosition(160, 45);
						opponent.rotateBy(90);
					}
				}
			}
		}

		this.deckSize = message.getDeckSize();
		this.turn = message.getTurn();
		this.matchId = matchId;


        //  Prepare card deck to draw
		deck = new BaseCard(new Sprite(game.assetManager.get("regularBlue.jpg", Texture.class)));
        stage.addActor(deck);
        deck.setPosition(55, 40);

        update();
    }

    public void update()
    {
		game.logger.info("turn: " + turn + ", turnCount: " + turnCount + ", opponents.length: " + opponents.length);

		if (turn == player.getDirection())
		{
			turnX = 80;
			turnY = 5;

			if (mustDraw <= 0)
			{
				enableHand();
			}
			enableDeck();
		}
		else
		{
			for (Opponent opponent : opponents)
			{
				if(opponent.getDirection() != player.getDirection())
				{
					if (turn == opponent.getDirection())
					{
						turnX = opponent.getX();
						turnY = opponent.getY();
					}
				}
			}
		}


    }

    public void drawCard(int cardId)
    {
    	mustDraw--;
        player.addCard(allCards.getCardById(cardId));
    }

    public void giveCard(int direction)
	{
		deckSize--;
		if (direction != player.getDirection())
		{
			game.logger.info("Player[" + direction + "] drew a card");
			BaseCard animationCard = pool.pop();
			animationCard.setPosition(deck.getX(), deck.getY());

			RunnableAction drawCardAction = new RunnableAction();
			drawCardAction.setRunnable(() -> opponents[direction].addActor(pool.pop()));

			animationCard.addAction(Actions.sequence(
					Actions.moveTo(opponents[direction].getX(), opponents[direction].getY(), 0.3f),
					Actions.moveTo(1000, 1000),
					drawCardAction));

			pool.add(animationCard);
		}
	}

	/**
	 * Play a card from the player who has the turn
	 * @param opponent	the person that plays the card
	 * @param cardId	id of playing card
	 */
	public void playCard(Opponent opponent, int cardId)
	{
		turnCount++;

		Card card = allCards.getCardById(cardId);
		card.clearListeners();

		stage.addActor(card);
		pile.add(card);

		card.setZIndex(100 + turnCount);   //  100 is arbitrary

		if (player != opponent)
		{
			card.setPosition(opponent.getX(), opponent.getY());
			card.addAction(Actions.moveTo(80, 40, 0.3f));
		}
		else
		{
			//	don't play the animation if player is this.player
			card.addAction(Actions.moveTo(80, 40));
		}
	}

	public Opponent[] getOpponents() { return opponents; }

	/**
	 * Add listener to cards that player can play. Grey out unplayable cards
	 */
	private void enableHand()
	{
		CardListener cardListener = new CardListener(this);

		boolean isFirstHand = turnCount < opponents.length;

		if (isFirstHand)
		{
			player.enableHand(cardListener, isFirstHand, null);
		}
		else
		{
			player.enableHand(cardListener, isFirstHand, pile.peek());
		}
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
						Actions.moveTo(80, 0, 0.3f), Actions.moveTo(1000, 1000)));

				game.logger.info("Sending draw card message");

				game.nakama.getSocket().sendMatchData(matchId, Opcode.DRAW_CARD.id, new byte[0]);

				//	clear listeners until server sends the card
				deck.clearListeners();

				event.handle();
				return true;
			}
		});
	}

	//	GETTERS AND SETTERS
	public int getTurn() { return turn; }

	public void setTurn(int turn) { this.turn = turn; }

	public Player getPlayer() { return player; }

	public BaseCard getDeck() { return deck; }

	public void setMustDraw(int mustDraw)
	{
		this.mustDraw = mustDraw;
	}

	public int getMustDraw()
	{
		return mustDraw;
	}

	//	SCREEN OVERRIDE METHODS
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        super.render(delta);

		game.batch.begin();
        game.font.draw(game.batch, Integer.toString(deckSize),55, 38);
        //game.font.draw(game.batch, Integer.toString(opponent.getHand().size), opponent.getX() + 20, opponent.getY() + 10);
        game.batch.end();

		game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		game.shapeRenderer.setColor(1, 0, 0, 1);
		game.shapeRenderer.circle(turnX, turnY, 5);

		game.shapeRenderer.set(ShapeRenderer.ShapeType.Line);
		game.shapeRenderer.setColor(Color.WHITE);
		game.shapeRenderer.rect(80, 40, 14, 21);
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
