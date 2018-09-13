package com.pisyedilionline.game;

import com.google.common.base.Function;
import com.google.common.util.concurrent.*;
import com.heroiclabs.nakama.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import PisYediliOnline.BuildConfig;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.UUID;

public class NakamaSessionManager
{
	private final Client client;
	private final PisYediliOnline game;

	private Session session;
	private SocketClient socket;

	private Logger logger;

	public NakamaSessionManager(final PisYediliOnline game)
	{
		this.game = game;

		client = new DefaultClient(BuildConfig.ServerKey, BuildConfig.Host, BuildConfig.Port, false);

		logger = LoggerFactory.getLogger(NakamaSessionManager.class);
	}


	public void createMatch()
	{
		final ListenableFuture<Match> createMatch = socket.createMatch();

		final Function<Match, Object> print = match ->
		{
			logger.info("Match created with ID: {}", match.getMatchId());
			return null;
		};

		Futures.transform(createMatch, print);
	}

	public void start()
	{
		// Lets check if we can restore a cached session.
		String sessionString = game.prefs.getString("nk.session", null);
		if (sessionString != null && !sessionString.isEmpty())
		{
			Session restoredSession = DefaultSession.restore(sessionString);
			if (session != null)
			{
				if (!session.isExpired(new Date()))
				{
					// Session was valid and is restored now.
					this.session = restoredSession;
					return;
				}
				logger.info("Session is expired.");
			}
			logger.warn("Session is null.");
		}

		String deviceId = UUID.randomUUID().toString();
		final ListenableFuture<Session> authentication = client.authenticateDevice(deviceId);

		final AsyncFunction<Session, Session> onAuthenticate = session ->
		{
			// Login was successful.
			// Store the session for later use.
			game.prefs.putString("nk.session", session.getAuthToken());
			game.prefs.flush();

			logger.info("Authentication is successful with token: {}", session.getAuthToken());

			this.socket = client.createSocket();
			return socket.connect(session, new AbstractClientListener()
			{
			});
		};

		final ListenableFuture<Session> authenticateFuture = Futures.transformAsync(authentication, onAuthenticate);

		final Function<Session, Object> assignSession = session ->
		{
			logger.info("soket kuruldu");
			this.session = session;
			game.setConnected(true);
			return null;
		};

		Futures.transform(authenticateFuture, assignSession);
	}
}