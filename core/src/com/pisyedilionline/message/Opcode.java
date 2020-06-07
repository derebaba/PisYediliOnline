package com.pisyedilionline.message;

public enum Opcode {
    GAME_INIT(1),
    DRAW_CARD(2),
	DRAW_CARD_BROADCAST(3),
	PLAY_CARD(4),
	PASS_TURN(5),
    SHUFFLE(6),
    END_GAME(7);

    public long id;

    Opcode(long id)
    {
        this.id = id;
    }

    public static Opcode getById(long id)
    {
        for(Opcode e : values()) {
            if(e.id == id) return e;
        }

        return null;
    }
}
