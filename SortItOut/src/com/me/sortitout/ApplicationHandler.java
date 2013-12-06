package com.me.sortitout;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class ApplicationHandler extends Game {
	
	
	
	private MenuScreen menuScreen;
	private GameScreen gameScreen;
	private GameObject gameObject;
	
	public static IReqHandler ExternalHandler;
	
	public ApplicationHandler(IReqHandler irh) {
		// TODO Auto-generated constructor stub
		ApplicationHandler.ExternalHandler = irh;
	}
	public ApplicationHandler() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void create() {
		// TODO Auto-generated method stub
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		gameObject = new GameObject(this, w, h);
		
		gameScreen = new GameScreen(this);
		menuScreen = new MenuScreen(this);
		//menuScreen.create();
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
}