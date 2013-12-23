package com.me.sortitout;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;


public class GestureHandler implements GestureListener {
	
	private GameObject gameObject;
	//private float ScreenWidth; 
	private float h;
	Rectangle tp;
	Vector2 vel = new Vector2();
	//OrthographicCamera camera;
	public boolean Init(ApplicationHandler ahandler) {
				
		gameObject = ahandler.getGameObject();
		//ScreenWidth = gameObject.getScreenWidth();
		h = gameObject.getScreenHeight();
		tp = new Rectangle();
		tp.width = gameObject.BLOCK_SIZE_PIX;
		tp.height = tp.width;
		return true;
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		tp.x = x;
		tp.y = h-y;
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
		Sprite sprite;
		for (Body block : gameObject.GetBlockList()) {
			sprite = gameObject.GetSpriteList().get((Integer) block.getUserData()); 
			if (sprite.getBoundingRectangle().contains(tp.x, tp.y)){
				vel.set(velocityX, -velocityY)
							.clamp(Config.MIN_SPEED, Config.MAX_SPEED);
							
				block.setLinearVelocity(vel);
				gameObject.setActiveItem(block);
				break;
			}
		}
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		Sprite sprite;
		
		for (Body block : gameObject.GetBlockList()) {
				sprite = gameObject.GetSpriteList().get((Integer) block.getUserData()); 
				if (sprite.getBoundingRectangle().contains(tp.x, tp.y)){
					vel.set(deltaX, -deltaY)
								.nor()
								.scl(Config.MAX_SPEED)
								.clamp(Config.MIN_SPEED, Config.MAX_SPEED);
					block.setLinearVelocity(vel);
					gameObject.setActiveItem(block);
					break;
				}
		}
		tp.x = x;
		tp.y = h-y;
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
