package com.pisyedilionline.screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.common.base.Function;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.Empty;
import com.heroiclabs.nakama.MatchmakerTicket;
import com.pisyedilionline.game.PisYediliOnline;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.concurrent.Executors;

public class MainMenuScreen extends BaseScreen
{
    private final TextButton findMatchButton;

    private boolean inMMQueue = false;
    private float deltaCounter = 0;
    private ListenableFuture<MatchmakerTicket> matchmakerTicketListenableFuture;
    private final TextField nameField;

    public MainMenuScreen(final PisYediliOnline game)
    {
        super(game);

        nameField = new TextField("", game.skin);
        nameField.setSize(400, 160);
        nameField.setPosition(400, 480);
        stage.addActor(nameField);

        findMatchButton = new TextButton("Find a Match", game.skin);
        findMatchButton.setSize(400, 160);
        findMatchButton.setPosition(400, 320);
        findMatchButton.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                if(!inMMQueue){
                    nameField.setDisabled(true);
                    ListenableFuture<Void> connect = game.nakama.start(nameField.getText());

                    FutureCallback<Void> addMatchmaker = new FutureCallback<Void>() {
                        @Override
                        public void onSuccess(@NullableDecl Void aVoid) {
                            String query = "*";
                            int minCount = 2;
                            int maxCount = 2;
                            matchmakerTicketListenableFuture = game.nakama.getSocket().addMatchmaker(minCount, maxCount, query);

                            game.logger.info("Started matchmaking...");

                            findMatchButton.setText("Abort Search");

                            deltaCounter = 0;
                        }

                        @Override
                        public void onFailure(Throwable throwable) {

                        }
                    };

                    Futures.addCallback(connect, addMatchmaker, Executors.newSingleThreadExecutor());
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
                    Futures.transform(matchmakerTicketListenableFuture, assignMatchmakerQueue, Executors.newSingleThreadExecutor());

                    findMatchButton.setText("Find a Match");
                    nameField.setDisabled(false);
                }
                inMMQueue = !inMMQueue;
                return true;
            }
        });
        stage.addActor(findMatchButton);
    }

    @Override
    public void render(float delta)
    {
        super.render(delta);
/*
        findMatchButton.setVisible(game.isConnected());
        nameField.setVisible(game.isConnected());

        if (!game.isConnected())
        {
            game.batch.begin();
            game.font.draw(game.batch, "Connecting", 10, 20);
            game.batch.end();
            return;
        }
*/
        if (inMMQueue){
            deltaCounter += delta;
            int seconds = (int)deltaCounter;

            game.batch.begin();
            game.font.draw(game.batch, "Searching for " + seconds
                            + (seconds < 2? " second" : " seconds"),
                    320, 320);
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