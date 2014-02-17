package com.me.sortitout;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;


public class GestureHandler implements GestureListener {
	
	private GameObject game;
	//private float ScreenWidth; 
	private float h;
	Rectangle tp;
	Vector2 vel = new Vector2();
	Vector2 dist = new Vector2();
	//OrthographicCamera camera;
	public boolean Init() {
				
		game = GameApp.gameObject;
		//ScreenWidth = gameObject.getScreenWidth();
		h = game.getScreenHeight();
		tp = new Rectangle();
		tp.width = GameObject.BLOCK_SIZE_PIX;
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
		/*Sprite sprite;
		for (Body block : game.GetBlockList()) {
			sprite = game.GetSpriteList().get((Integer) block.getUserData()); 
			if (sprite.getBoundingRectangle().contains(tp.x, tp.y)){
				vel.set(velocityX, -velocityY)
							.clamp(Config.MIN_SPEED, Config.MAX_SPEED);
							
				block.setLinearVelocity(vel);
				game.setActiveItem(block);
				break;
			}
		}*/
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		Sprite sprite;
		dist.set(deltaX, -deltaY);
		if (dist.len()<1) {
			return false;
		}
		for (Body block : game.GetBlockList()) {
				sprite = game.GetSpriteList().get((Integer) block.getUserData()); 
				if (sprite.getBoundingRectangle().contains(tp.x, tp.y)){
					vel.set(dist.div(GameObject.BOX_TO_WORLD))
								.scl(50f)
								.clamp(0f, Assets.MAX_SPEED);
					block.setLinearVelocity(vel);
					game.setActiveItem(block);
					break;
				}
		}
		tp.x = x;
		tp.y = h-y;
		return true;
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
		return game;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
}
