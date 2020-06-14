package com.pisyedilionline.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.google.gson.Gson;
import com.heroiclabs.nakama.*;
import com.heroiclabs.nakama.Error;
import com.heroiclabs.nakama.api.ChannelMessage;
import com.heroiclabs.nakama.api.NotificationList;
import com.pisyedilionline.message.*;
import com.pisyedilionline.screen.GameScreen;

import java.util.ArrayList;
import java.util.List;

public class PisClientListener implements SocketListener
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
    public void onMatchmakerMatched(final MatchmakerMatched matchmakerMatched)
    {
        game.matchId = matchmakerMatched.getMatchId();

        game.nakama.getSocket().joinMatch(matchmakerMatched.getMatchId());
        game.logger.info("joining...");

        username = matchmakerMatched.getSelf().getPresence().getUsername();

        game.logger.debug("found match with ID: " + matchmakerMatched.getMatchId() + ", username: "  + username);
    }

    @Override
    public void onMatchData(final MatchData matchData)
    {
        String data = matchData.getData() == null ? null : new String(matchData.getData());
        Opcode opCode = Opcode.getById(matchData.getOpCode());
        game.logger.info("Received message with opCode " + opCode);
        game.logger.info("Message data: " + data);

        switch (opCode)
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
                    gameScreen.drawCard(drawCardMessage);
                });
                break;

            case DRAW_CARD_BROADCAST:
                DrawCardBroadcastMessage drawCardBroadcastMessage = gson.fromJson(data, DrawCardBroadcastMessage.class);
                Gdx.app.postRunnable(() -> gameScreen.giveCard(drawCardBroadcastMessage));
                break;

            case PLAY_CARD:
                PlayCardMessage playCardMessage = gson.fromJson(data, PlayCardMessage.class);
                Gdx.app.postRunnable(() -> gameScreen.playCard(playCardMessage));
                break;

            case PASS_TURN:
                PassTurnMessage passTurnMessage = gson.fromJson(data, PassTurnMessage.class);
                Gdx.app.postRunnable(() ->
                {
                    gameScreen.passTurn(passTurnMessage);
                });
                break;

            case SHUFFLE:
                ShuffleMessage shuffleMessage = gson.fromJson(data, ShuffleMessage.class);
                Gdx.app.postRunnable(() ->
                {
                    gameScreen.shufflePileIntoDeck(shuffleMessage);
                });
                break;

            case END_GAME:
                gameScreen.endGame(data);
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
