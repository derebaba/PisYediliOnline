package com.pisyedilionline.message;

public class ChatMessageServer {

    private String text;

    private int senderDirection;

    public ChatMessageServer (String text)
    {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public int getSenderDirection() {
        return senderDirection;
    }
}
