package com.pisyedilionline.screen;

import com.pisyedilionline.game.PisYediliOnline;

public class SplashScreen extends BaseScreen
{

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
        game.font.draw(game.batch, "Pis Yedili - 7", 80, 400);
        game.font.draw(game.batch, "Yukleniyor", 80, 160);

        // display loading information
        float progress = game.assetManager.getProgress();
        game.font.draw(game.batch, Float.toString(progress), 800, 160);
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