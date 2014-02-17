package com.me.sortitout;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.me.sortitout.screens.GameScreen;
import com.me.sortitout.screens.HighScoresScreen;
import com.me.sortitout.screens.MenuScreen;
import com.me.sortitout.screens.SplashScreen;

public class GameApp extends Game {

	public static MenuScreen menuScreen;
	public static GameScreen gameScreen;
	public static GameObject gameObject;
	public static HighScoresScreen hsScreen;
	
	public static IReqHandler ExternalHandler;
	public static GameApp handler;
	
	public GameApp(IReqHandler irh) {
		GameApp.ExternalHandler = irh;
	}
	public GameApp() {
		if (handler == null) {
			handler = this;
		}
	}
	@Override
	public void create() {
		//Loading native libraries
		GdxNativesLoader.load();
		Settings.load();
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		//Splash
		this.setScreen(new SplashScreen());
		//Initialize configuration and resources
		Assets.Load();
		//
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);
		
		gameObject = new GameObject(w, h);
		
		gameScreen = new GameScreen();
		menuScreen = new MenuScreen();
		hsScreen = new HighScoresScreen();

	}

	@Override
	public void dispose() {
		Settings.save();
		gameObject.dispose();
		gameScreen.dispose();
		hsScreen.dispose();
		Assets.dispose();
		super.dispose();
		menuScreen.dispose();		
	}
}