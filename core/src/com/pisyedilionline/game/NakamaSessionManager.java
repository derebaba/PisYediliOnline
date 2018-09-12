package com.pisyedilionline.game;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.heroiclabs.nakama.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import PisYediliOnline.BuildConfig;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.UUID;

public class NakamaSessionManager {
    private final Client client;
    private final PisYediliOnline game;
    private Session session;

    private Logger nakamaLogger;

    public NakamaSessionManager(final PisYediliOnline game)
    {
        this.game = game;

        client = new DefaultClient(BuildConfig.ServerKey, BuildConfig.Host, BuildConfig.Port, false);

        nakamaLogger = LoggerFactory.getLogger(NakamaSessionManager.class);
    }

    public void start() {
        // Lets check if we can restore a cached session.
        String sessionString = game.prefs.getString("nk.session", null);
        if (sessionString != null && !sessionString.isEmpty()) {
            Session restoredSession = DefaultSession.restore(sessionString);
            if (!session.isExpired(new Date())) {
                // Session was valid and is restored now.
                this.session = restoredSession;
                return;
            }
        }

        String deviceId = UUID.randomUUID().toString();
        final ListenableFuture<Session> authentication = client.authenticateDevice(deviceId);

        Futures.addCallback(authentication, new FutureCallback<Session>()
        {
            @Override
            public void onSuccess(@Nullable Session session)
            {
                // Login was successful.
                // Store the session for later use.
                game.prefs.putString("nk.session", session.getAuthToken());
                nakamaLogger.info("Authentication is successful with token: {}", session.getAuthToken());

                SocketClient socket = client.createSocket();
                final ListenableFuture<Session> socketConnection = socket.connect(session,
                        new AbstractClientListener() {});

                Futures.addCallback(socketConnection, new FutureCallback<Session>()
                {
                    @Override
                    public void onSuccess(@Nullable Session session)
                    {
                        nakamaLogger.info("soket kuruldu");
                    }

                    @Override
                    public void onFailure(Throwable throwable)
                    {
                        nakamaLogger.error("Socket connection failed. See stack trace:");
                        throwable.printStackTrace();
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable)
            {
                nakamaLogger.error("Authentication failed. See stack trace:");
                throwable.printStackTrace();
            }
        });
    }
}