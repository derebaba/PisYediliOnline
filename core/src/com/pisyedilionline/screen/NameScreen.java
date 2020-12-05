package com.pisyedilionline.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.common.base.Function;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.heroiclabs.nakama.MatchmakerTicket;
import com.pisyedilionline.game.PisYediliOnline;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.concurrent.Executors;

public class NameScreen extends BaseScreen {

    private Label nameLabel, infoLabel;
    private final TextField nameField;
    private Button startButton;

    public NameScreen(PisYediliOnline game) {
        super(game);

        nameLabel = new Label("Isim: ", new Label.LabelStyle(game.font, Color.WHITE));
        nameLabel.setSize(200, 160);
        nameLabel.setPosition(300, 480);
        stage.addActor(nameLabel);

        nameField = new TextField("", game.skin);
        nameField.setSize(350, 160);
        nameField.setPosition(450, 480);
        stage.addActor(nameField);

        startButton = new TextButton("Baglan", game.skin);
        startButton.setSize(400, 160);
        startButton.setPosition(350, 320);
        startButton.addListener(new ClickListener()
        {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                startButton.setDisabled(true);
                infoLabel.setText("Baglaniyor");

                ListenableFuture<Void> connect = game.nakama.start(nameField.getText());
                FutureCallback<Void> connectCallback = new FutureCallback<Void>() {
                    @Override
                    public void onSuccess(@NullableDecl Void aVoid) {
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                game.setScreen(new MainMenuScreen(game));
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        startButton.setDisabled(false);
                    }
                };

                Futures.addCallback(connect, connectCallback, MoreExecutors.directExecutor());
            }
        });
        stage.addActor(startButton);

        infoLabel = new Label("", new Label.LabelStyle(game.font, Color.WHITE));
        infoLabel.setSize(200, 160);
        infoLabel.setPosition(300, 100);
        stage.addActor(infoLabel);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void show() {

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
}
