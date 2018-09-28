package com.pisyedilionline.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
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

	//	game variables
	private Array<Opponent> opponents;
	private int deckSize = 0;
	private int direction;	//	this player's chair position
	private int turn;	//	whose turn is it
	private boolean clockwise = true;
	private String username, matchId;

	//	cards
	private AllCards allCards;
	private Array<Card> hand, pile;
	private Array<BaseCard> pool;
	private BaseCard deck;

	private float turnX = 0, turnY = 0;	//	turnün kimde olduğunu gösteren yuvarlağın pozisyonu

    public GameScreen(final PisYediliOnline game, GameStartMessage message, String username, String matchId) {
        super(game);

		allCards = new AllCards(game);
		pile = new Array<Card>();
		pool = new Array<>(52);
		for (int i = 0; i < 52; i++)
		{
			pool.add(new BaseCard(new Sprite(game.assetManager.get("regularBlue.jpg", Texture.class))));
		}

		//	initialize hand
		hand = new Array<Card>();
		for (int id : message.getCards()) {
			Card card = allCards.getCardById(id);
			hand.add(card);
			stage.addActor(card);
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
        deck.setPosition(70, 35);

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

		if (turn == direction)
		{
			deck.addListener(event ->
			{
				game.nakama.getSocket().sendMatchData(matchId, Opcode.DRAW_CARD.id, new byte[0]);
				deck.clearListeners();
				return true;
			});
		}
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
            card.setPosition(30 + 10 * i, 5);
            card.setBounds(card.getX(), card.getY(),
                    card.getSprite().getWidth(), card.getSprite().getHeight());
        }
    }

    //  TODO: kendi sırası değilse false dönsün

    public void drawCard(int cardId)
    {
        hand.add(allCards.getCardById(cardId));
    }

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
