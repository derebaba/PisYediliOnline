package com.pisyedilionline.game;

import com.google.protobuf.InvalidProtocolBufferException;
import com.heroiclabs.nakama.*;
import com.heroiclabs.nakama.Error;
import com.heroiclabs.nakama.api.ChannelMessage;
import com.heroiclabs.nakama.api.NotificationList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PisClientListener implements ClientListener
{
	private final PisYediliOnline game;
	List<UserPresence> connectedOpponents;

	public PisClientListener(final PisYediliOnline game)
	{
		this.game = game;

		connectedOpponents = new ArrayList<UserPresence>();
	}

	@Override
	public void onDisconnect(Throwable throwable)
	{
		throwable.printStackTrace();
	}

    @Override
    public void onError(Error error) {
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
		game.logger.info("found match with ID: " + matchmakerMatched.getMatchId());
		((MainMenuScreen)game.getScreen()).setFoundMatch(true);

		game.nakama.getSocket().joinMatch(matchmakerMatched.getMatchId());
		game.logger.info("joining...");
	}

	@Override
	public void onMatchData(final MatchData matchData)
	{
		try
		{
			MessageProtos.Person p = MessageProtos.Person.parseFrom(matchData.getData());
			game.logger.info("Received match data " + p.getCards() + " with opcode " + matchData.getOpCode());
		}
		catch (InvalidProtocolBufferException e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void onMatchPresence(MatchPresenceEvent matchPresenceEvent)
	{
		game.logger.info("present");
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
