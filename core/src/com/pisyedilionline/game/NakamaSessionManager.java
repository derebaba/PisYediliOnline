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

	private Logger nakamaLogger;

	public NakamaSessionManager(final PisYediliOnline game)
	{
		this.game = game;

		client = new DefaultClient(BuildConfig.ServerKey, BuildConfig.Host, BuildConfig.Port, false);

		nakamaLogger = LoggerFactory.getLogger(NakamaSessionManager.class);
	}

	/*
		public String createMatch()
		{
			ListenableFuture<Match> createMatch = socket.createMatch();

			Futures.transform(createMatch, );
			return match.getMatchId();
		}
	*/
	public void start()
	{
		// Lets check if we can restore a cached session.
		String sessionString = game.prefs.getString("nk.session", null);
		if (sessionString != null && !sessionString.isEmpty())
		{
			Session restoredSession = DefaultSession.restore(sessionString);
			if (!session.isExpired(new Date()))
			{
				// Session was valid and is restored now.
				this.session = restoredSession;
				return;
			}

			nakamaLogger.info("Session is expired.");
		}

		String deviceId = UUID.randomUUID().toString();
		final ListenableFuture<Session> authentication = client.authenticateDevice(deviceId);

		final Function<Session, SocketClient> onAuthenticate = session ->
		{
			// Login was successful.
			// Store the session for later use.
			game.prefs.putString("nk.session", session.getAuthToken());
			game.prefs.flush();

			nakamaLogger.info("Authentication is successful with token: {}", session.getAuthToken());

			return client.createSocket();
		};

		final ListenableFuture<SocketClient> authenticateFuture = Futures.transform(authentication, onAuthenticate);

		final AsyncFunction<SocketClient, Session> connectSocket = socket ->
		{
			this.socket = socket;
			return socket.connect(session, new AbstractClientListener()
			{
			});
		};

		final ListenableFuture<Session> socketFuture = Futures.transformAsync(authenticateFuture, connectSocket);

		final Function<Session, Object> assignSession = session ->
		{
			nakamaLogger.info("soket kuruldu");
			this.session = session;
			return null;
		};

		Futures.transform(socketFuture, assignSession);
	}
}