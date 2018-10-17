package com.pisyedilionline.actor;

import com.pisyedilionline.game.PisYediliOnline;
import com.pisyedilionline.message.PlayerMessage;
import org.jetbrains.annotations.NotNull;

public class Player extends Opponent {

    public Player(final PisYediliOnline game, @NotNull PlayerMessage message)
    {
        super(game, message);
    }
}
