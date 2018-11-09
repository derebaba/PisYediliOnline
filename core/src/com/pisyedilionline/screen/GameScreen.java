package com.pisyedilionline.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.pisyedilionline.actor.BaseCard;
import com.pisyedilionline.actor.BaseCardPool;
import com.pisyedilionline.actor.MainPlayer;
import com.pisyedilionline.actor.Player;
import com.pisyedilionline.game.AllCards;
import com.pisyedilionline.actor.Card;
import com.pisyedilionline.game.PisYediliOnline;
import com.pisyedilionline.message.DrawCardBroadcastMessage;
import com.pisyedilionline.message.DrawCardMessage;
import com.pisyedilionline.message.GameStartMessage;
import com.pisyedilionline.message.Opcode;
import com.pisyedilionline.message.PassTurnMessage;
import com.pisyedilionline.message.PlayerMessage;
import com.pisyedilionline.message.ShuffleMessage;

public class GameScreen extends BaseScreen
{
    public static final int MAX_ALLOWED_DRAW_CARD_COUNT = 3;
    public static final int DRAW_CARD_COUNT_PER_7 = 3;

	final Card.Suit[] SUITS = Card.Suit.values();

	//	GAME VARIABLES
	private Player players[];
	private MainPlayer mainPlayer;
	private int deckSize = 0;
	private int turn;	//	whose turn is it
	private int pile7Count = 0;
    private int jiletSuit = 0;
	private boolean lastCardA = false;


	/**
	 * how many turns have passed
	 */
	private int turnCount = 0;

	private String matchId;

	//	CARDS
	private AllCards allCards;
	private Array<Card> pile;
	private BaseCardPool baseCardPool = null;
	private BaseCard deck;

	//	TODO: turn yuvarlağı yerine Player'a bi indicator ekle
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

		Sprite baseCardSprite = new Sprite(game.assetManager.get("regularBlue.jpg", Texture.class));
		baseCardPool = new BaseCardPool(baseCardSprite, 52, 52);

		//	initialize players
		players = new Player[message.getPlayers().length];
		for (int i = 0; i < message.getPlayers().length; i++)
		{
			PlayerMessage playerMessage = message.getPlayers()[i];
			if (playerMessage.getUsername().equals(username))
			{
			    game.logger.debug("My user name:" + username);
				mainPlayer = new MainPlayer(this, playerMessage);

				for (int id : message.getCards()) {
					Card card = allCards.getCardById(id);
					mainPlayer.addCard(card);
				}

				players[mainPlayer.getDirection()] = mainPlayer;
				stage.addActor(mainPlayer);
			}
			else
			{
				Player player = new Player(this, playerMessage);

				for (int j = 0; j < playerMessage.getCardCount(); j++)
				{
					BaseCard baseCard = baseCardPool.obtain();
					player.addActor(baseCard);
				}

				players[player.getDirection()] = player;
				stage.addActor(player);
			}
		}

		mainPlayer.setPosition(80, 0);
		//	set position of players
		if (players.length == 2)
		{
			//	if there is only 1 opponent, let him sit across
			for (Player player : players)
			{
				if (player.getDirection() != mainPlayer.getDirection())
				{
					player.setPosition(80, 69);
				}
			}
		}
		else
		{
			for (Player player : players)
			{
				if (player.getDirection() != mainPlayer.getDirection())
				{
					if (player.getDirection() == (mainPlayer.getDirection() + 1) % message.getPlayers().length)
					{
						player.setPosition(0, 45);
						player.rotateBy(-90);
					}
					else if (player.getDirection() == (mainPlayer.getDirection() + 2) % message.getPlayers().length)
					{
						player.setPosition(80, 69);
					}
					else if (player.getDirection() == (mainPlayer.getDirection() + 3) % message.getPlayers().length) {
						player.setPosition(160, 45);
						player.rotateBy(90);
					}
				}
			}
		}

		this.deckSize = message.getDeckSize();
		this.turn = message.getTurn();
		this.turnCount = message.getTurnCount();
		this.matchId = matchId;

        //  Prepare card deck to draw
		deck = new BaseCard(new Sprite(game.assetManager.get("regularBlue.jpg", Texture.class)));
        stage.addActor(deck);
        deck.setPosition(55, 40);

        update();
    }

    public void update()
    {
		game.logger.info("turn: " + turn + ", turnCount: " + turnCount + ", players.length: " + players.length);

		if (turn == mainPlayer.getDirection())
		{
			turnX = 80;
			turnY = 5;
            CardListener cardListener = new CardListener(this);

            boolean isFirstHand = turnCount <= players.length;

            if (lastCardA){ // A is played previously
                if (!mainPlayer.lastCardADrawn){
                    enableDeck(true);
                }
                else{
                    enableDeck(false);
                    game.nakama.getSocket().sendMatchData(game.matchId,
                            Opcode.PASS_TURN.id, new byte[0]);
                }
            }
           /* else if (mainPlayer.drawn7Count > 0 && mainPlayer.drawn7Count < pile7Count * DRAW_CARD_COUNT_PER_7){ // mainPlayer has drawn for 7 and needs to draw more
                enableDeck(true);
            }*/
            else if (mainPlayer.enableHand(cardListener, isFirstHand, pile7Count, jiletSuit, pile.size == 0 ? null : pile.peek())){
                enableDeck(false);
            }
            else if (isFirstHand){ // first hand, mainPlayer can draw as many
                enableDeck(true);
            }
            else if (mainPlayer.drawnRegularCardCount < MAX_ALLOWED_DRAW_CARD_COUNT){ // mainPlayer can draw more
                enableDeck(true);
            }
            else{
                enableDeck(false);
                game.nakama.getSocket().sendMatchData(game.matchId,
                        Opcode.PASS_TURN.id, new byte[0]);
            }
		}
		else
		{
		    mainPlayer.disableHand();
		    enableDeck(false);
		    for (Player player : players)
			{
				if(player.getDirection() != mainPlayer.getDirection())
				{
					if (turn == player.getDirection())
					{
						turnX = player.getX();
						turnY = player.getY();
					}
				}
			}
		}
    }

    public void drawCard(DrawCardMessage drawCardMessage)
    {
		for (int i = 0; i < drawCardMessage.getdrawnCards().length; i++){
			Card card = allCards.getCardById(drawCardMessage.getdrawnCards()[i]);

			RunnableAction drawCardAction = new RunnableAction();
			drawCardAction.setRunnable(() -> {
				mainPlayer.addCard(card);
				if (lastCardA){
					mainPlayer.lastCardADrawn = true;
				}
				else if (mainPlayer.drawn7Count < pile7Count * DRAW_CARD_COUNT_PER_7){
					mainPlayer.drawn7Count++;
				}
				else{
					mainPlayer.drawnRegularCardCount++;
				}
				update();
			});
			card.addAction(Actions.sequence(
					Actions.moveTo(deck.getX(), deck.getY()),
					Actions.delay(0.1f * i),
					Actions.moveTo(mainPlayer.getX(), mainPlayer.getY(), 0.3f),
					drawCardAction));
		}
    }

    public void giveCard(DrawCardBroadcastMessage drawCardBroadcastMessage)
	{
		deckSize = drawCardBroadcastMessage.getDeckSize();
		if (drawCardBroadcastMessage.getDirection() != mainPlayer.getDirection())
		{
			game.logger.info("Player[" + drawCardBroadcastMessage.getDirection() + "] drew a card");

			final Player player = players[drawCardBroadcastMessage.getDirection()];

			for (int i = 0; i < drawCardBroadcastMessage.getCardCount(); i++){
				final BaseCard baseCard = baseCardPool.obtain();
				RunnableAction drawCardAction = new RunnableAction();
				stage.addActor(baseCard);

				drawCardAction.setRunnable(() -> player.addActor(baseCard));

				baseCard.addAction(Actions.sequence(
						Actions.moveTo(deck.getX(), deck.getY()),
						Actions.delay(0.1f * i),
						Actions.moveTo(player.getX(), player.getY(), 0.3f),
						drawCardAction));
			}
		}
	}

	public void passTurn(PassTurnMessage passTurnMessage){
    	turnCount++;
        turn = passTurnMessage.getDirection();
        pile7Count = passTurnMessage.getPile7Count();
        jiletSuit = passTurnMessage.getJiletSuit();
        lastCardA = passTurnMessage.isLastCardA();
		turnCount = passTurnMessage.getTurnCount();
        if (turn == mainPlayer.getDirection()){
            mainPlayer.lastCardADrawn = false;
            mainPlayer.drawnRegularCardCount = 0;
            mainPlayer.drawn7Count = 0;
        }

        update();
    }

    public void shufflePileIntoDeck(ShuffleMessage shuffleMessage){
        deckSize = shuffleMessage.getDeckSize();
        pile.clear();
        pile.add(allCards.getCardById(shuffleMessage.getTopCard()));

        update();
    }

	public void playCard(int direction, int cardId)
	{
		Card card = allCards.getCardById(cardId);
		stage.addActor(card);
		playCard(direction, card);
	}

	public void playCard(int direction, Card card)
	{
		card.clearListeners();

		pile.add(card);

		card.setZIndex(100 + turnCount);   //  100 is arbitrary

		Player player = players[direction];

		//int marginX = MathUtils.random(-2,3);
		//int marginY = MathUtils.random(-2,3);
		int marginX = 0;
		int marginY = 0;

		if (mainPlayer.getDirection() !=  player.getDirection())
		{
			BaseCard baseCard = (BaseCard) player.getChildren().pop();
			baseCardPool.free(baseCard);

			card.addAction(Actions.sequence(
					Actions.moveTo(player.getX(), player.getY()),
					Actions.moveTo(80 + marginX, 40 + marginY, 0.3f)));
		}
		else
		{
			//	don't play the animation if mainPlayer is this.mainPlayer
			card.resetColor();
			card.addAction(Actions.moveTo(80 + marginX, 40 + marginY));
		}
	}

	public Player[] getPlayers() { return players; }

	public void enableDeck(boolean enabled)
	{
		if (enabled){
			deck.getSprite().setColor(Color.GREEN);
			deck.addListener(new InputListener()
			{
				@Override
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

				    if (deckSize == 0){
                        game.nakama.getSocket().sendMatchData(matchId, Opcode.SHUFFLE.id, new byte[0]);
                    }
                    else{
						int cardCountToBeDrawn = 1;
						int requiredCardDrawsFor7 = pile7Count * DRAW_CARD_COUNT_PER_7 - mainPlayer.drawn7Count;
				    	if(requiredCardDrawsFor7 > 0){ // needs to draw for 7
							cardCountToBeDrawn = Math.min(requiredCardDrawsFor7, deckSize);
						}
                        game.logger.info("Sending draw card message. Card count: " + cardCountToBeDrawn);
						 byte[] data = Integer.toString(cardCountToBeDrawn).getBytes();
                        game.nakama.getSocket().sendMatchData(matchId, Opcode.DRAW_CARD.id, data);
                    }
                    // todo implement multiple card draws in the case of seven
					//	clear listeners until server sends the card
                    enableDeck(false);

					event.handle();
					return true;
				}
			});
		}
		else{
			deck.getSprite().setColor(Color.GRAY);
			deck.clearListeners();
		}
	}



	//	GETTERS AND SETTERS

	public MainPlayer getMainPlayer() { return mainPlayer; }

	public BaseCard getDeck() { return deck; }



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
