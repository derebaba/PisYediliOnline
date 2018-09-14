package com.pisyedilionline.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

	public MainMenuScreen(final PisYediliOnline game)
	{
		super(game);

		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

		findMatchButton = new TextButton("Find a match", skin);
		stage.addActor(findMatchButton);
		findMatchButton.setSize(50, 20);
		findMatchButton.setPosition(50, 50);
		findMatchButton.getLabel().setFontScale(0.5f);
		findMatchButton.addListener(new InputListener()
		{
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				String query = "*";
				int minCount = 2;
				int maxCount = 2;

				final ListenableFuture<MatchmakerTicket> matchmakerFuture = game.nakama.getSocket()
						.addMatchmaker(minCount, maxCount, query);
				game.logger.info("Started matchmaking...");

				final Function<MatchmakerTicket, Object> onMatchmade = matchmakerTicket ->
				{
					game.setScreen(new GameScreen(game));
					game.logger.info("Found a match with ticket: " + matchmakerTicket.getTicket());
					dispose();
					return null;
				};

				Futures.transform(matchmakerFuture, onMatchmade);

				return true;
			}
		});
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