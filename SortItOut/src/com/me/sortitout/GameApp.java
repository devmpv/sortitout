package com.me.sortitout;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.me.sortitout.screens.GameScreen;
import com.me.sortitout.screens.MenuScreen;
import com.me.sortitout.screens.ScoresScreen;
import com.me.sortitout.screens.SplashScreen;

public class GameApp extends Game {

	public static MenuScreen menuScreen;
	public static GameScreen gameScreen;
	public static GameObject gameObject;
	public static ScoresScreen scoresScreen;
	
	public static IReqHandler ExternalHandler;
	public static GameApp handler;
	
	public GameApp(IReqHandler irh) {
		GameApp.ExternalHandler = irh;
		if (handler == null) {
			handler = this;
		}
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
		scoresScreen = new ScoresScreen();

	}

	@Override
	public void dispose() {
		Settings.save();
		super.dispose();
		gameObject.dispose();
		gameScreen.dispose();
		scoresScreen.dispose();
		menuScreen.dispose();
		Assets.dispose();
		handler = null;
	}
}