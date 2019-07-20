package com.pisyedilionline.screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.heroiclabs.nakama.MatchmakerTicket;
import com.pisyedilionline.game.PisYediliOnline;

public class MainMenuScreen extends BaseScreen
{
    private TextButton findMatchButton;

    private boolean inMMQueue = false;
    private float deltaCounter = 0;
    private ListenableFuture<MatchmakerTicket> matchmakerTicketListenableFuture;

    public MainMenuScreen(final PisYediliOnline game)
    {
        super(game);

        findMatchButton = new TextButton("Find a Match", game.skin);
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
                if(!inMMQueue){
                    String query = "*";
                    int minCount = 2;
                    int maxCount = 2;

                    matchmakerTicketListenableFuture = game.nakama.getSocket().addMatchmaker(minCount, maxCount, query);
                    game.logger.info("Started matchmaking...");

                    findMatchButton.setText("Abort Search");
                    deltaCounter = 0;
                }
                else{

                    final Function<MatchmakerTicket, Object> assignMatchmakerQueue = matchmakerTicket ->
                    {
                        if (matchmakerTicket != null){
                            game.nakama.getSocket().removeMatchmaker(matchmakerTicket.getTicket());
                            game.logger.info("Cancelled matchmaking...");
                        }
                        return null;
                    };
                    Futures.transform(matchmakerTicketListenableFuture, assignMatchmakerQueue);

                    findMatchButton.setText("Find a Match");
                }
                inMMQueue = !inMMQueue;
                return true;
            }
        });
    }

    @Override
    public void render(float delta)
    {
        super.render(delta);

        findMatchButton.setVisible(game.isConnected());

        if (!game.isConnected())
        {
            game.batch.begin();
            game.font.draw(game.batch, "Connecting", 10, 20);
            game.batch.end();
            return;
        }

        if (inMMQueue){
            deltaCounter += delta;
            int seconds = (int)deltaCounter;

            game.batch.begin();
            game.font.draw(game.batch, "Searching for " + seconds
                            + (seconds < 2? " second" : " seconds"),
                    40, 40);
            game.batch.end();
            return;
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