package com.me.sortitout;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.me.sortitout.ApplicationHandler;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Sort It Out";
		cfg.useGL20 = false;
		cfg.width = 480;
		cfg.height = 600;
		cfg.samples = 4;
		cfg.resizable = false;
		cfg.vSyncEnabled = true;
		new LwjglApplication(new ApplicationHandler(), cfg);
	}
}
