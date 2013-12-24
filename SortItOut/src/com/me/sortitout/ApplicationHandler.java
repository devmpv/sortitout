package com.me.sortitout;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxNativesLoader;

public class ApplicationHandler extends Game {

	private MenuScreen menuScreen;
	private GameScreen gameScreen;
	private GameObject gameObject;
	
	public static IReqHandler ExternalHandler;
	
	public ApplicationHandler(IReqHandler irh) {
		ApplicationHandler.ExternalHandler = irh;
	}
	public ApplicationHandler() {
	}
	@Override
	public void create() {
		//Loading native libraries
		GdxNativesLoader.load();
		
		//Initialize configuration and resources
		Config.getInst();
		//
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		gameObject = new GameObject(this, w, h);
		
		gameScreen = new GameScreen(this);
		menuScreen = new MenuScreen(this);
		//Sleep for 2 seconds
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			
		}
		this.setScreen(menuScreen);

	}
	public GameObject getGameObject() {
		return gameObject;
	}
	public GameScreen getGameScreen() {
		return gameScreen;
	}
	public void showMenu() {
		
		this.setScreen(menuScreen);
	}
	public void showGame() {
		
		this.setScreen(gameScreen);
	}
	@Override
	public void dispose() {		
		gameObject.dispose();
		gameScreen.dispose();
		Config.dispose();
		super.dispose();
		menuScreen.dispose();		
	}
}