package com.pisyedilionline.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.heroiclabs.nakama.MatchmakerTicket;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class MainMenuScreen extends BaseScreen
{
	TextButton findMatchButton;
	Skin skin;
	private boolean foundMatch = false;

	public MainMenuScreen(final PisYediliOnline game)
	{
		super(game);

		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

		findMatchButton = new TextButton("Mac bul", skin);
		stage.addActor(findMatchButton);

		findMatchButton.setVisible(false);
		findMatchButton.setSize(50, 20);
		findMatchButton.setPosition(50, 50);
		findMatchButton.getLabel().setFontScale(0.5f);
		findMatchButton.addListener(new ClickListener()
		{
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				String query = "*";
				int minCount = 2;
				int maxCount = 2;

				game.nakama.getSocket()
						.addMatchmaker(minCount, maxCount, query);
				game.logger.info("Started matchmaking...");

				return true;
			}
		});
	}

	public void setFoundMatch(boolean foundMatch)
	{
		this.foundMatch = foundMatch;
	}

	public boolean getFoundMatch() { return foundMatch; }

	@Override
	public void render(float delta)
	{
		super.render(delta);
		if (foundMatch)
		{
			game.setScreen(new GameScreen(game));
			dispose();
		}

		findMatchButton.setVisible(game.isConnected());

		if (!game.isConnected())
		{
			game.batch.begin();
			game.font.draw(game.batch, "Baglaniyor", 10, 20);
			game.batch.end();
		}
	}

	@Override
	public void show()
	{

	}

	@Override
	public void pause()
	{

	}

	@Override
	public void resume()
	{

	}

	@Override
	public void hide()
	{

	}
}