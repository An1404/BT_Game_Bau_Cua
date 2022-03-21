package com.btl.ttltmang.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.btl.ttltmang.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Main.APP_WIDTH;
		config.height = Main.APP_HEIGHT;//445
		config.title = Main.TITLE;
		config.resizable = false;
		config.backgroundFPS = 10;
		config.foregroundFPS = 60;
		config.addIcon("logo.png", Files.FileType.Internal);
		new LwjglApplication(new Main(), config);
	}
}
