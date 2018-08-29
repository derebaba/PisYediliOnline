package com.pisyedilionline.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pisyedilionline.game.PisYediliOnline;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Pis yedili";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new PisYediliOnline(), config);
	}
}
