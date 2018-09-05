package com.pisyedilionline.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;


public class PisYediliOnline extends Game {

    public Logger logger;
    public AssetManager assetManager;

	public SpriteBatch batch;
	public BitmapFont font;

	@Override
	public void create() {
        logger = new Logger("genel");
        assetManager = new AssetManager();

		batch = new SpriteBatch();

		//Use LibGDX's default Arial font.
		font = new BitmapFont();
		font.getData().setScale(0.5f);
		font.setColor(Color.WHITE);
		font.setUseIntegerPositions(false);

        logger.setLevel(Logger.DEBUG);

		this.setScreen(new MainMenuScreen(this));

		assetManager.load("deck.png", Texture.class);
        assetManager.load("regularBlue.jpg", Texture.class);
	}

	public void render() {
		super.render(); //important!
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
		this.getScreen().dispose();
	}

}