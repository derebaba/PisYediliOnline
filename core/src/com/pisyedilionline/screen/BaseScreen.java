package com.pisyedilionline.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pisyedilionline.game.PisYediliOnline;

public abstract class BaseScreen implements Screen {

    static final int WORLD_WIDTH = 1280;
    static final int WORLD_HEIGHT = 720;

    public final PisYediliOnline game;
    protected OrthographicCamera camera;

    protected Stage stage;

    public BaseScreen(final PisYediliOnline game)
    {
        this.game = game;

        camera = new OrthographicCamera();

        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera));

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height){
        stage.getViewport().update(width, height);
        camera.position.set(camera.viewportWidth / 2,camera.viewportHeight / 2,0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.2f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
		game.shapeRenderer.setProjectionMatrix(camera.combined);

        stage.draw();
        stage.act(delta);
    }

    @Override
    public void dispose()
    {
        stage.dispose();
    }

    public Stage getStage() { return stage; }
}
