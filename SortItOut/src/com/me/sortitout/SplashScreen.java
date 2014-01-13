package com.me.sortitout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen {
	private float w, h;
	private ApplicationHandler app;
	private SpriteBatch spriteBatch;
    private Texture splash;

	public SplashScreen(ApplicationHandler applicationHandler) {
		// TODO Auto-generated constructor stub
		app = applicationHandler;
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		spriteBatch = new SpriteBatch();
        splash = new Texture(Gdx.files.internal("data/splash.jpg"));
        splash.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        spriteBatch.draw(splash, w/2-w/4, h/2-w/4, w/2, w/2);
        spriteBatch.end();
        
        if(Gdx.input.justTouched()) 
        	if (app.getMenuScreen()!=null)
                app.setScreen(app.getMenuScreen());
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		spriteBatch.dispose();
		splash.dispose();
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
