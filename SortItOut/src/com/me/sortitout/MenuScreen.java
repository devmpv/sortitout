package com.me.sortitout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
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
	private TextButton buttonContinue;	
	private Music menuMusic;
	
	public MenuScreen(ApplicationHandler applicationHandler) {
		appHandler = applicationHandler;
		create();
	}

	public void create () {
		
	        //float dWidth = appHandler.getGameObject().getScreenWidth(); 
	        //float dHeight = GameObject.BLOCK_SIZE*appHandler.getGameObject().BOX_TO_WORLD*3;
			menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/menu.mp3"));
			Config.getInst().menuMusic = menuMusic;
			float buttonWidth=appHandler.getGameObject().BLOCK_SIZE_PIX*2;
			float buttonHeight=appHandler.getGameObject().BLOCK_HALF_PIX + appHandler.getGameObject().BLOCK_HALF_PIX/2;
			stage = new Stage();
	        //Gdx.input.setInputProcessor(stage);
	        
	        Table table = new Table();
	        stage.addActor(table);
	        
	        skin = new Skin(Gdx.files.internal("data/skin.json"));
	        skin.getFont("normaltext").setScale(appHandler.getGameObject().getScreenWidth()/480);
	        //skin.add("background", new Texture("textures/bubble1.png"));
	        table.setFillParent(true); 
	        table.setBackground(skin.getTiledDrawable("background"));

	        exitDialog = 
	        	new Dialog("", skin, "default") {
					protected void result (Object obj) {
						if (obj.equals(true)){
							Gdx.app.exit();
						}else {
							Config.getInst().playSnd(Config.SND_BUTTON);
						}
					}
				}.text("Exit game?").button(" Exit ", true).button(" Back ", false).key(Keys.ENTER, true)
				.key(Keys.ESCAPE, false);
	        final TextButton button1 = new TextButton("New game", skin);
	        final Widget widget1 = new Widget();
	        final TextButton button2 = new TextButton("Continue", skin);
	        
	        buttonContinue = button2;
	        button2.setDisabled(true);
	        final TextButton button3 = new TextButton("Exit", skin);
	        table.add(button1).width(buttonWidth).height(buttonHeight);
	        table.row();
	        table.add(widget1).height(buttonHeight/2);
	        table.row();
	        table.add(button2).width(buttonWidth).height(buttonHeight);
	        table.row();
	        table.add(widget1).height(buttonHeight/2);
	        table.row();
	        table.add(button3).width(buttonWidth).height(buttonHeight);
	        
	        Config.getInst().menuButton = new Button(skin, "button-mus");
	        //Config.getInst().menuButton.setSize(buttonHeight, buttonHeight);
	        
	        final Button sndMuteButton = new Button(skin, "button-snd");
	        //sndMuteButton.setSize(buttonHeight, buttonHeight);
	        Table buttonTable = new Table();
	        buttonTable.bottom().left();
	        buttonTable.add(Config.getInst().menuButton).size(buttonHeight);
	        buttonTable.add(sndMuteButton).size(buttonHeight);
	        stage.addActor(buttonTable);
	        
	        Config.getInst().menuButton.addListener(new ClickListener() {
	    		public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
	    			//super.touchDown(event, x, y, pointer, button);
	    			Config.getInst().playSnd(Config.SND_BUTTON);
	    			if (Config.getInst().menuButton.isChecked()){
	    				Config.getInst().pauseMusic();
	    			}else {
	    				Config.getInst().playMusic(menuMusic);
	    			}
	    				
	        	}
	    	});	        
	        sndMuteButton.addListener(new ClickListener() {
        		public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        			//super.touchDown(event, x, y, pointer, button);
        			if (sndMuteButton.isChecked()) {
        				Config.mute = true;
        			} else {
        				Config.mute = false;
        			}
        				
            	}
        	});
	        button1.addListener(new ClickListener() {
	        		public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
	        			//super.touchDown(event, x, y, pointer, button);
	        			appHandler.getGameObject().shuffle();
	        			button2.setDisabled(false);
	        			Config.getInst().playSnd(Config.SND_BUTTON);
	        			appHandler.showGame();
	            	}
	        	});
	        button2.addListener(new ClickListener() {
        		public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        			//super.touchDown(event, x, y, pointer, button);
        			if (!button2.isDisabled()) {
        				Config.getInst().playSnd(Config.SND_BUTTON);
        				appHandler.showGame();
        			}
            	}
        	});
	        button3.addListener(new ClickListener() {
	        	public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        			//super.touchDown(event, x, y, pointer, button);
	        		Config.getInst().playSnd(Config.SND_BUTTON);
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
		menuMusic.dispose();
		stage.dispose();
		skin.dispose();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);    
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        //Table.drawDebug(stage); // Enables debug lines for tables.
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		buttonContinue.setDisabled(!appHandler.getGameObject().isActive());
		if (!Config.getInst().menuButton.isChecked()) {
			menuMusic.play();
		}
	}

	@Override
	public void hide() {
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
