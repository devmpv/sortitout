package com.me.sortitout;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.GdxNativesLoader;


public class GestureHandler implements GestureListener {
	
	private GameObject gameObject;
	//private float ScreenWidth; 
	private float ScreenHeight;
	Rectangle tpointer;
	//OrthographicCamera camera;
	public boolean Init(ApplicationHandler ahandler) {
				
		gameObject = ahandler.getGameObject();
		//ScreenWidth = gameObject.getScreenWidth();
		ScreenHeight = gameObject.getScreenHeight();
		//Loading native libraries
		GdxNativesLoader.load();
		tpointer = new Rectangle();
		tpointer.width = gameObject.BLOCK_SIZE_PIX;
		tpointer.height = tpointer.width;
		return true;
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		tpointer.x = x;
		tpointer.y = ScreenHeight-y;
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		Sprite sprite;
		for (Body block : gameObject.GetBlockList()) {
				sprite = gameObject.GetSpriteList().get((Integer) block.getUserData()); 
				if (sprite.getBoundingRectangle().contains(tpointer.x, tpointer.y)){
					//Limiting speed to WORLD_MAX_SPEED
					if (Math.abs(deltaX) > Config.WORLD_MAX_SPEED) {
						deltaX = Math.signum(deltaX)*Config.WORLD_MAX_SPEED;
					}
					if (Math.abs(deltaY) > Config.WORLD_MAX_SPEED) {
						deltaY = Math.signum(deltaY)*Config.WORLD_MAX_SPEED;
					}

					block.setLinearVelocity(new Vector2(deltaX*Config.BLOCK_SIZE, -deltaY*Config.BLOCK_SIZE));
					gameObject.setActiveItem(block);
					break;
				}
		}
		tpointer.x = x;
		tpointer.y = ScreenHeight-y;
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		
		return false;
	}
	public GameObject getGame(){
		return gameObject;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
}
