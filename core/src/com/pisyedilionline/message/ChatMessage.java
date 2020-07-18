package com.pisyedilionline.message;

public class ChatMessage {

    public static final String REYIZ = "reyiz en cok degistirilen karttir.";

    private String sender;
    private int messageCode;

    public ChatMessage(int messageCode, String sender)
    {
        this.messageCode = messageCode;
        this.sender = sender;
    }

    public String getSender() { return sender; }

    public String getMessage()
    {
        switch (messageCode)
        {
            case 7:
                return "7?";
            case 10:
                return "10?";
            case 11:
                return "jilet?";
            case 14:
                return REYIZ;
            default:
                return String.valueOf(messageCode);
        }
    }
}
