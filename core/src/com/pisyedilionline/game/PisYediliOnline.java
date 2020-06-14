package com.pisyedilionline.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.pisyedilionline.screen.SplashScreen;

import java.io.File;


public class PisYediliOnline extends Game {

	public ShapeRenderer shapeRenderer;
    public Logger logger;
    public AssetManager assetManager;
    public Preferences prefs;

	public SpriteBatch batch;
	public BitmapFont font;
	public Skin skin;
	public String matchId = "";

	public NakamaSessionManager nakama;

	private boolean isConnected = false;

	@Override
	public void create() {
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);

		//	configure logger
		org.apache.log4j.PropertyConfigurator.configure("../../core/src/main/resources/log4j.properties");
        logger = LoggerFactory.getLogger(this.getClass());
        logger.debug("LibGdx version: " + com.badlogic.gdx.Version.VERSION);

        assetManager = new AssetManager();
		//	load assets
		assetManager.load("deck.png", Texture.class);
		assetManager.load("regularBlue.jpg", Texture.class);
		assetManager.load("club.png", Texture.class);
		assetManager.load("diamond.png", Texture.class);
		assetManager.load("heart.png", Texture.class);
		assetManager.load("spade.png", Texture.class);
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        prefs =  Gdx.app.getPreferences("SessionInfo");

        nakama = new NakamaSessionManager(this);

		batch = new SpriteBatch();

		//Use LibGDX's default Arial font.
		font = new BitmapFont();
		font.getData().setScale(0.5f);
		font.setColor(Color.WHITE);
		font.setUseIntegerPositions(false);

		this.setScreen(new SplashScreen(this));
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
		shapeRenderer.dispose();
		assetManager.dispose();
		this.getScreen().dispose();
	}

}