package com.pisyedilionline.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.google.gson.Gson;
import com.heroiclabs.nakama.*;
import com.heroiclabs.nakama.Error;
import com.heroiclabs.nakama.api.ChannelMessage;
import com.heroiclabs.nakama.api.NotificationList;
import com.pisyedilionline.message.DrawCardMessage;
import com.pisyedilionline.message.GameStartMessage;
import com.pisyedilionline.message.Opcode;
import com.pisyedilionline.screen.GameScreen;
import com.pisyedilionline.screen.MainMenuScreen;

import java.util.ArrayList;
import java.util.List;

public class PisClientListener implements ClientListener
{
	private final PisYediliOnline game;
	private GameScreen gameScreen;
	private List<UserPresence> connectedOpponents;
	private Gson gson;
	private String username;

	public PisClientListener(final PisYediliOnline game)
	{
		this.game = game;

		gson = new Gson();
		connectedOpponents = new ArrayList<UserPresence>();
	}

	@Override
	public void onDisconnect(Throwable throwable)
	{
		game.logger.error("Disconnected");
		throwable.printStackTrace();
	}

    @Override
    public void onError(Error error) {
		game.logger.error("Client listener error");
		error.printStackTrace();
    }

    @Override
	public void onChannelMessage(ChannelMessage channelMessage)
	{

	}

	@Override
	public void onChannelPresence(ChannelPresenceEvent channelPresenceEvent)
	{

	}

	@Override
	public void onMatchmakeMatched(final MatchmakerMatched matchmakerMatched)
	{
		game.matchId = matchmakerMatched.getMatchId();

		game.logger.info("found match with ID: " + matchmakerMatched.getMatchId());

		game.nakama.getSocket().joinMatch(matchmakerMatched.getMatchId());
		game.logger.info("joining...");

		username = matchmakerMatched.getSelf().getPresence().getUsername();
	}

	@Override
	public void onMatchData(final MatchData matchData)
	{
		String data = new String(matchData.getData());

		game.logger.info("Received message with opCode " + matchData.getOpCode());
		game.logger.info("Message data: " + data);

		switch (Opcode.getById(matchData.getOpCode()))
		{
			case GAME_INIT:
				GameStartMessage gameStartMessage = gson.fromJson(data, GameStartMessage.class);

				Gdx.app.postRunnable(() ->
				{
					Screen screen = game.getScreen();
					GameScreen gameScreen = new GameScreen(game, gameStartMessage, username, matchData.getMatchId());
					this.gameScreen = gameScreen;
					game.setScreen(gameScreen);
					screen.dispose();
				});
			break;
			case DRAW_CARD:
				DrawCardMessage drawCardMessage = gson.fromJson(data, DrawCardMessage.class);

				Gdx.app.postRunnable(() ->
				{
					gameScreen.drawCard(drawCardMessage.getCard());
					gameScreen.update();
				});
			break;
			case DRAW_CARD_BROADCAST:
				int direction = Integer.parseInt(data);
			break;
		}
	}

	@Override
	public void onMatchPresence(final MatchPresenceEvent matchPresenceEvent)
	{
		game.logger.info("Present in match");


/*
		long opCode = 1;
		String data = "{\"message\":\"Hello world\"}";
		game.nakama.getSocket().sendMatchData(matchPresenceEvent.getMatchId(), opCode, data.getBytes());*/
/*
		connectedOpponents.addAll(matchPresenceEvent.getJoins());
		for (UserPresence leave : matchPresenceEvent.getLeaves()) {
			for (int i = 0; i < connectedOpponents.size(); i++) {
				if (connectedOpponents.get(i).getUserId().equals(leave.getUserId())) {
					connectedOpponents.remove(i);
				}
			}
		};
*/
	}

	@Override
	public void onNotifications(NotificationList notificationList)
	{

	}

	@Override
	public void onStatusPresence(StatusPresenceEvent statusPresenceEvent)
	{

	}

	@Override
	public void onStreamPresence(StreamPresenceEvent streamPresenceEvent)
	{

	}

	@Override
	public void onStreamData(StreamData streamData)
	{

	}
}
