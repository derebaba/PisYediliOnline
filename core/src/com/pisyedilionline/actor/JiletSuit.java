package com.pisyedilionline.actor;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class JiletSuit extends BaseCard {

    private int selectedSuit;

    public JiletSuit(Sprite sprite, int selectedSuit) {
        super(sprite);
        this.selectedSuit = selectedSuit;
    }

    public int getSelectedSuit() {
        return selectedSuit;
    }
}
