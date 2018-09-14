package com.pisyedilionline.game;

import com.badlogic.gdx.Gdx;

public class SplashScreen extends BaseScreen {

    public SplashScreen(final PisYediliOnline game) {
        super(game);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(game.assetManager.update()) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }

        super.render(delta);

        game.batch.begin();
        game.font.draw(game.batch, "Pis Yedili - 7", 10, 50);
        game.font.draw(game.batch, "Yukleniyor", 10, 20);

        // display loading information
        float progress = game.assetManager.getProgress();
        game.font.draw(game.batch, Float.toString(progress), 100, 20);
        game.batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        super.dispose();
    }
}