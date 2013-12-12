package com.me.sortitout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuScreen implements Screen {

	private Skin skin;
	private Stage stage;
	private ApplicationHandler appHandler;
	private Dialog exitDialog;
	public boolean isActive = false;
	private Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/menu.mp3"));
	private Sound ButtonSound = Gdx.audio.newSound(Gdx.files.internal("sounds/button.wav"));
	
	public MenuScreen(ApplicationHandler applicationHandler) {
		// TODO Auto-generated constructor stub
		appHandler = applicationHandler;
		create();
	}

	public void create () {
		
	        //float dWidth = appHandler.getGameObject().getScreenWidth(); 
	        //float dHeight = GameObject.BLOCK_SIZE*appHandler.getGameObject().BOX_TO_WORLD*3;
			stage = new Stage();
	        Gdx.input.setInputProcessor(stage);
	        
	        Table table = new Table();
	        table.setFillParent(true);       
	        stage.addActor(table);
	        skin = new Skin(Gdx.files.internal("data/skin.json"));
	        skin.add("background", new Texture("textures/bubble1.png"));
	        table.setBackground(skin.getTiledDrawable("background"));

	        exitDialog = 
	        	new Dialog("", skin, "default") {
					protected void result (Object obj) {
						if (obj.equals(true)){
							Gdx.app.exit();
						}
					}
				}.text("Are you sure?").button("Exit", true).button("Back", false).key(Keys.ENTER, true)
				.key(Keys.ESCAPE, false);
	        final TextButton button1 = new TextButton("New game", skin);
	        final Widget widget1 = new Widget();
	        final TextButton button2 = new TextButton("Continue", skin);
	        button2.setDisabled(true);
	        final TextButton button3 = new TextButton("Exit", skin);
	        table.add(button1).minWidth(250).maxHeight(70);
	        table.row();
	        table.add(widget1);
	        table.row();
	        table.add(button2).minWidth(250).maxHeight(70);
	        table.row();
	        table.add(widget1);
	        table.row();
	        table.add(button3).minWidth(250).maxHeight(70);
	        // Add widgets to the table here.

	        button1.addListener(new ClickListener() {
	        		public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
	        			//super.touchDown(event, x, y, pointer, button);
	        			appHandler.getGameObject().Shuffle();
	        			button2.setDisabled(false);
	        			ButtonSound.play();
	        			appHandler.showGame();
	            	}
	        	});
	        button2.addListener(new ClickListener() {
        		public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        			//super.touchDown(event, x, y, pointer, button);
        			if (!button2.isDisabled()) {
        				ButtonSound.play();
        				appHandler.showGame();
        			}
            	}
        	});
	        button3.addListener(new ClickListener() {
	        	public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        			//super.touchDown(event, x, y, pointer, button);
	        		ButtonSound.play();
	        		exitDialog.show(stage);
            	}
        	});
	        stage.addListener(new InputListener() {
	        	public boolean keyUp (InputEvent event, int keycode) {
	        		if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
	        			exitDialog.show(stage);
	        		}
	        		return false;
	        	}
	        });
	}
	
	public void resize (int width, int height) {
	        stage.setViewport(width, height, true);
	}


	public void dispose() {
	        stage.dispose();
	        isActive = false;
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);    
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        //Table.drawDebug(stage); // This is optional, but enables debug lines for tables.
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		Gdx.input.setInputProcessor(stage);
		menuMusic.play();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		menuMusic.pause();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

}
