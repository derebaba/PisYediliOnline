package com.pisyedilionline.game;

public enum Opcode {
    GAME_INIT(1);

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
