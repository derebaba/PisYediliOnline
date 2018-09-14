package com.pisyedilionline.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;


public class PisYediliOnline extends Game {

    public Logger logger;
    public AssetManager assetManager;
    public Preferences prefs;

	public SpriteBatch batch;
	public BitmapFont font;

	public NakamaSessionManager nakama;

	private boolean isConnected = false;

	@Override
	public void create() {
        logger = new Logger("Genel");
		logger.setLevel(Logger.DEBUG);

        assetManager = new AssetManager();
        prefs =  Gdx.app.getPreferences("SessionInfo");

        nakama = new NakamaSessionManager(this);
		nakama.start();

		batch = new SpriteBatch();

		//Use LibGDX's default Arial font.
		font = new BitmapFont();
		font.getData().setScale(0.5f);
		font.setColor(Color.WHITE);
		font.setUseIntegerPositions(false);

		this.setScreen(new MainMenuScreen(this));

		//	load assets
		assetManager.load("deck.png", Texture.class);
        assetManager.load("regularBlue.jpg", Texture.class);
	}

	public void setConnected(boolean isConnected)
	{
		this.isConnected = isConnected;
	}

	public boolean isConnected()
	{
		return isConnected;
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