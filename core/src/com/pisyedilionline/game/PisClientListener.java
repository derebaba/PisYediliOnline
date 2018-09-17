package com.pisyedilionline.game;

import com.heroiclabs.nakama.*;
import com.heroiclabs.nakama.api.ChannelMessage;
import com.heroiclabs.nakama.api.NotificationList;

public class PisClientListener implements ClientListener
{
	private final PisYediliOnline game;

	public PisClientListener(final PisYediliOnline game)
	{
		this.game = game;
	}

	@Override
	public void onDisconnect(Throwable throwable)
	{

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
	public void onMatchmakeMatched(MatchmakerMatched matchmakerMatched)
	{
		game.logger.info("found match");
		((MainMenuScreen)game.getScreen()).setFoundMatch(true);
	}

	@Override
	public void onMatchData(MatchData matchData)
	{

	}

	@Override
	public void onMatchPresence(MatchPresenceEvent matchPresenceEvent)
	{

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
