package com.me.sortitout.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.sortitout.ApplicationHandler;
import com.me.sortitout.tween.SpriteAccessor;

public class SplashScreen implements Screen {
	private float w, h;
	private ApplicationHandler app;
	private SpriteBatch spriteBatch;
    private Sprite splash;
    private TweenManager tweenManager;

	public SplashScreen(ApplicationHandler applicationHandler) {
		app = applicationHandler;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		tweenManager.update(delta);
        spriteBatch.begin();
        	splash.draw(spriteBatch);
        spriteBatch.end();
        if (tweenManager.getRunningTweensCount() == 0 || Gdx.input.justTouched()) 
            app.setScreen(app.getMenuScreen());
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {
		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Texture texture = new Texture(Gdx.files.internal("data/splash.jpg"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		spriteBatch = new SpriteBatch();
        splash = new Sprite(texture);
        splash.setSize(w/2, w/2);
        splash.setPosition(w/2-w/4, h/2-w/4);
        splash.setOrigin(w/4, w/4);
        //Fade && Rotate
		Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
		Tween.set(splash, SpriteAccessor.ROTATE).target(720).start(tweenManager);
		Tween.to(splash, SpriteAccessor.ALPHA, 2).target(1).delay(2).start(tweenManager);
		Tween.to(splash, SpriteAccessor.ROTATE, 2).target(0).delay(2).start(tweenManager);
		Tween.to(splash, SpriteAccessor.ALPHA, 2).target(0).delay(6).start(tweenManager);
	}

	@Override
	public void hide() {
		spriteBatch.dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
