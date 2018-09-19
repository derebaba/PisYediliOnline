package com.pisyedilionline.game;

import com.heroiclabs.nakama.*;
import com.heroiclabs.nakama.Error;
import com.heroiclabs.nakama.api.ChannelMessage;
import com.heroiclabs.nakama.api.NotificationList;

import java.util.ArrayList;
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

	}

    @Override
    public void onError(Error error) {

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
	public void onMatchData(MatchData matchData)
	{
		game.logger.info("Received match data " + matchData.getData() + " with opcode " + matchData.getOpCode());
	}

	@Override
	public void onMatchPresence(MatchPresenceEvent matchPresenceEvent)
	{
		game.logger.info("present");
		connectedOpponents.addAll(matchPresenceEvent.getJoins());
		for (UserPresence leave : matchPresenceEvent.getLeaves()) {
			for (int i = 0; i < connectedOpponents.size(); i++) {
				if (connectedOpponents.get(i).getUserId().equals(leave.getUserId())) {
					connectedOpponents.remove(i);
				}
			}
		};
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
