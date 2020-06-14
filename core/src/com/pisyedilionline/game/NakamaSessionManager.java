package com.pisyedilionline.game;

import com.google.common.base.Function;
import com.google.common.util.concurrent.*;
import com.google.protobuf.Empty;
import com.heroiclabs.nakama.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NakamaSessionManager
{
    private final Client client;
    private final PisYediliOnline game;

    private String deviceId;
    private Session session;
    private SocketClient socket;


    public NakamaSessionManager(final PisYediliOnline game)
    {
        this.game = game;

        //client = new DefaultClient(BuildConfig.ServerKey, BuildConfig.Host, BuildConfig.Port, false);
        client = new DefaultClient("defaultkey", "localhost", 7349, false);	//	uncomment for local development

        deviceId = UUID.randomUUID().toString();
    }

    public SocketClient getSocket() { return socket; }

    public ListenableFuture<Void> start(String username)
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

        final ListenableFuture<Session> authentication = client.authenticateDevice(deviceId, username);

        final AsyncFunction<Session, Session> onAuthenticate = session ->
        {
            // Login was successful.
            // Store the session for later use.
            game.prefs.putString("nk.session", session.getAuthToken());
            game.prefs.flush(); // TODO: sonra kaldırılacak

            game.logger.info("Authentication is successful.");

            socket = client.createSocket();
            return socket.connect(session, new PisClientListener(game));
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        final ListenableFuture<Session> authenticateFuture = Futures.transformAsync(authentication, onAuthenticate, executor);

        final Function<Session, Void> assignSession = session ->
        {
            game.logger.info("soket kuruldu");
            this.session = session;
            game.setConnected(true);

            return null;
        };

        return Futures.transform(authenticateFuture, assignSession, executor);
    }
}