package com.pisyedilionline.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;
import com.pisyedilionline.actor.*;
import com.pisyedilionline.game.AllCards;
import com.pisyedilionline.game.PisYediliOnline;
import com.pisyedilionline.message.*;

public class GameScreen extends BaseScreen
{
    public static final int MAX_ALLOWED_DRAW_CARD_COUNT = 3;
    public static final int DRAW_CARD_COUNT_PER_7 = 3;
    public static final int DECK_X = 55, DECK_Y = 40;
    public static final int PILE_X = 80, PILE_Y = 40;

    //	PLAYER POSITIONS
    public static final int NORTH_X = 80, NORTH_Y = 69;
    public static final int SOUTH_X = 80, SOUTH_Y = 0;
    public static final int WEST_X = 0, WEST_Y = 45;
    public static final int EAST_X = 160, EAST_Y = 45;

	final Card.Suit[] SUITS = Card.Suit.values();

	//	GAME VARIABLES
	private Player players[];
	private MainPlayer mainPlayer;
	private int deckSize = 0;
	private int turn;	//	whose turn is it
	private int pile7Count = 0;
    private int jiletSuit = -1;
	private boolean lastCardA = false;
	private JiletSuit club, diamond, heart, spade;
	private int beforeJiletCardId;


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

		mainPlayer.setPosition(SOUTH_X, SOUTH_Y);
		//	set position of players
		if (players.length == 2)
		{
			//	if there is only 1 opponent, let him sit across
			for (Player player : players)
			{
				if (player.getDirection() != mainPlayer.getDirection())
				{
					player.setPosition(NORTH_X, NORTH_Y);
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
						player.setPosition(WEST_X, WEST_Y);
						player.rotateBy(-90);
					}
					else if (player.getDirection() == (mainPlayer.getDirection() + 2) % message.getPlayers().length)
					{
						player.setPosition(NORTH_X, NORTH_Y);
					}
					else if (player.getDirection() == (mainPlayer.getDirection() + 3) % message.getPlayers().length) {
						player.setPosition(EAST_X, EAST_Y);
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
        deck.setPosition(DECK_X, DECK_Y);

        ClickListener suitListener = new ClickListener()
		{
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				JiletSuit jiletSuit = (JiletSuit) event.getTarget();
				PlayCardMessage message = new PlayCardMessage(beforeJiletCardId, mainPlayer.getDirection(), jiletSuit.getSelectedSuit());
				game.nakama.getSocket().sendMatchData(game.matchId,
						Opcode.PLAY_CARD.id, new Gson().toJson(message).getBytes());
				club.remove();
				diamond.remove();
				heart.remove();
				spade.remove();
				return true;
			}
		};
        club = new JiletSuit(new Sprite(game.assetManager.get("club.png", Texture.class)), 0);
		diamond = new JiletSuit(new Sprite(game.assetManager.get("diamond.png", Texture.class)), 1);
		heart = new JiletSuit(new Sprite(game.assetManager.get("heart.png", Texture.class)), 2);
		spade = new JiletSuit(new Sprite(game.assetManager.get("spade.png", Texture.class)), 3);

		club.addListener(suitListener);
		diamond.addListener(suitListener);
		heart.addListener(suitListener);
		spade.addListener(suitListener);

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

            if (pile.size > 0)
			{
				mainPlayer.setZIndex(pile.peek().getZIndex() + 1);
			}

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
		RunnableAction updateAction = new RunnableAction();
		updateAction.setRunnable(() -> {
			if (lastCardA){
				mainPlayer.lastCardADrawn = true;
			}
			else if (mainPlayer.drawn7Count < pile7Count * DRAW_CARD_COUNT_PER_7){
				mainPlayer.drawn7Count += drawCardMessage.getdrawnCards().length;
			}
			else{
				mainPlayer.drawnRegularCardCount++;
			}
			update();
		});

		for (int i = 0; i < drawCardMessage.getdrawnCards().length; i++){
			Card card = allCards.getCardById(drawCardMessage.getdrawnCards()[i]);

			RunnableAction drawCardAction = new RunnableAction();
			drawCardAction.setRunnable(() -> {
				mainPlayer.addCard(card);
			});

			Action actions = Actions.sequence(
					Actions.moveTo(deck.getX(), deck.getY()),
					Actions.delay(0.1f * i),
					Actions.moveTo(mainPlayer.getX(), mainPlayer.getY(), 0.3f),
					drawCardAction);

			if(i == drawCardMessage.getdrawnCards().length - 1){
				((SequenceAction) actions).addAction(updateAction);
			}
			card.addAction(actions);
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

	public void playCard(PlayCardMessage message)
	{
		Card card = allCards.getCardById(message.getCardId());
		stage.addActor(card);
		playCard(message.getPlayerDirection(), card);
		jiletSuit = message.getJiletSuit();
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
					Actions.moveTo(PILE_X + marginX, PILE_Y + marginY, 0.3f)));
		}
		else
		{
			//	don't play the animation if mainPlayer is this.mainPlayer
			card.resetColor();
			card.addAction(Actions.moveTo(PILE_X + marginX, PILE_Y + marginY));
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
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

					enableDeck(false);
				    if (deckSize == 0){
                        game.nakama.getSocket().sendMatchData(matchId, Opcode.SHUFFLE.id, new byte[0]);
						 game.logger.info("Sending shuffle message");
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

	public void showSuits(int cardId)
	{
		club.setPosition(25, 45);
		diamond.setPosition(25, 25);
		heart.setPosition(110, 45);
		spade.setPosition(110, 25);

		stage.addActor(club);
		stage.addActor(diamond);
		stage.addActor(heart);
		stage.addActor(spade);

		beforeJiletCardId = cardId;
	}


	//	GETTERS AND SETTERS

	public MainPlayer getMainPlayer() { return mainPlayer; }

	public BaseCard getDeck() { return deck; }

	public int getTurnCount() {
		return turnCount;
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
		game.shapeRenderer.rect(PILE_X, PILE_Y, 14, 21);
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
