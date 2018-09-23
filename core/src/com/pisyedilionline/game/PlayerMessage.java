package com.pisyedilionline.game;

class PlayerMessage
{
    String username;
    int cardCount;
    int direction;

    @Override
    public String toString() {
        return "username: " + username + " cardCount = " + cardCount + " direction = " + direction;
    }
}
