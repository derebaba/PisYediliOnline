package com.pisyedilionline.game;

import com.google.common.base.Function;
import com.google.common.util.concurrent.*;
import com.heroiclabs.nakama.*;
import PisYediliOnline.BuildConfig;

import java.util.Date;
import java.util.UUID;

public class NakamaSessionManager
{
	private final Client client;
	private final PisYediliOnline game;

	private Session session;
	private SocketClient socket;


	public NakamaSessionManager(final PisYediliOnline game)
	{
		this.game = game;

		//client = new DefaultClient(BuildConfig.ServerKey, BuildConfig.Host, BuildConfig.Port, false);
		client = new DefaultClient("defaultkey", "localhost", 7349, false);	//	uncomment for local development
	}

	public SocketClient getSocket() { return socket; }

	public void start()
	{
		// Lets check if we can restore a cached session.
		/*
		String sessionString = game.prefs.getString("nk.session", null);
		if (sessionString != null && !sessionString.isEmpty())
		{
			Session restoredSession = DefaultSession.restore(sessionString);

			if (!restoredSession.isExpired(new Date()))
			{
				// Session was valid and is restored now.v
				session = restoredSession;
				socket = client.createSocket();
				game.logger.info("Session is restored.");
				game.setConnected(true);
				return;
			}
			game.logger.info("Session is expired.");
		}
		*/
		String deviceId = UUID.randomUUID().toString();
		final ListenableFuture<Session> authentication = client.authenticateDevice(deviceId);

		final AsyncFunction<Session, Session> onAuthenticate = session ->
		{
			// Login was successful.
			// Store the session for later use.
			game.prefs.putString("nk.session", session.getAuthToken());
			game.prefs.flush();

			game.logger.info("Authentication is successful.");

			socket = client.createSocket();
			return socket.connect(session, new PisClientListener(game));
		};

		final ListenableFuture<Session> authenticateFuture = Futures.transformAsync(authentication, onAuthenticate);

		final Function<Session, Object> assignSession = session ->
		{
			game.logger.info("soket kuruldu");
			this.session = session;
			game.setConnected(true);
			return null;
		};

		Futures.transform(authenticateFuture, assignSession);
	}
}