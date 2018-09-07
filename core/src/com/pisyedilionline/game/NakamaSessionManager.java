package com.pisyedilionline.game;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.JdkFutureAdapters;
import com.google.common.util.concurrent.ListenableFuture;
import com.heroiclabs.nakama.*;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.UUID;

public class NakamaSessionManager {
    private final Client client;
    private final PisYediliOnline game;
    private Session session;

    public NakamaSessionManager(final PisYediliOnline game)
    {
        this.game = game;

        client = new DefaultClient("mynewkey", "127.0.0.1", 7349, false);
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

        final ListenableFuture<Session> future = client.authenticateDevice(deviceId);

        Futures.addCallback(future, new FutureCallback<Session>()
        {
            @Override
            public void onSuccess(@Nullable Session session)
            {
                // Login was successful.
                // Store the session for later use.
                game.prefs.putString("nk.session", session.getAuthToken());
                System.out.println(session.getAuthToken());
            }

            @Override
            public void onFailure(Throwable throwable)
            {
                System.out.println("sıçtı");
                throwable.printStackTrace();
            }
        });
    }
}

/*
import java.util.Date;
import java.util.UUID;

public class NakamaSessionManager {
    private final Client client;
    private Session session;
    private final PisYediliOnline game;

    public NakamaSessionManager(final PisYediliOnline game)
    {
        this.game = game;

        client = DefaultClient.builder("defaultkey")
                .host("127.0.0.1")
                .port(7349)
                .timeout(4000)
                .build();

        System.out.println("build");
    }

    public void start() {
        // Lets check if we can restore a cached session.
        String sessionString = game.prefs.getString("nk.session", null);
        game.logger.info("start");
        if (sessionString != null && !sessionString.isEmpty()) {
            Session restoredSession = DefaultSession.restore(sessionString);
            if (!session.isExpired(new Date().getTime())) {
                // Session was valid and is restored now.
                this.session = restoredSession;
                System.out.println("not expired: " + this.session.getToken());
                return;
            }
        }

        String deviceId = UUID.randomUUID().toString();
        //System.out.println(deviceId);

        AuthenticateMessage authMsg = AuthenticateMessage.Builder.device(deviceId);

        client.register(authMsg).addCallback(arg -> {
            // Login was successful.
            // Store the session for later use.
            game.prefs.putString("nk.session", arg.getToken());
            System.out.println(arg.getToken());
            return null;
        });
    }
}
*/