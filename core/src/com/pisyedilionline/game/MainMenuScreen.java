package com.pisyedilionline.game;

import com.badlogic.gdx.Gdx;

public class MainMenuScreen extends BaseScreen {

    public MainMenuScreen(final PisYediliOnline game) {
        super(game);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        super.render(delta);

        game.batch.begin();
        game.font.draw(game.batch, "Pis Yedili - 7", 10, 50);
        game.font.draw(game.batch, "Baslamak için dokun", 10, 20);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
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

    }

}