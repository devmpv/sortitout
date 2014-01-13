package com.me.sortitout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuScreen implements Screen {

	private Stage stage;
	private ApplicationHandler appHandler;
	private Dialog exitDialog;
	private TextButton buttonContinue;
	
	public MenuScreen(ApplicationHandler applicationHandler) {
		appHandler = applicationHandler;
		create();
	}

	public void create () {
		
	        //float dWidth = appHandler.getGameObject().getScreenWidth(); 
	        //float dHeight = GameObject.BLOCK_SIZE*appHandler.getGameObject().BOX_TO_WORLD*3;
			
			float buttonWidth=appHandler.getGameObject().BLOCK_SIZE_PIX*2;
			float buttonHeight=appHandler.getGameObject().BLOCK_HALF_PIX + appHandler.getGameObject().BLOCK_HALF_PIX/2;
			stage = new Stage();
	        
	        Table table = new Table();
	        stage.addActor(table);
	        
	        
	        table.setFillParent(true); 
	        table.setBackground(Assets.skin.getTiledDrawable("background"));

	        exitDialog = 
	        	new Dialog("", Assets.skin, "default") {
					protected void result (Object obj) {
						if (obj.equals(true)){
							Gdx.app.exit();
						}else {
							Assets.playSnd(Assets.buttonSound);
						}
					}
				}.text("Exit game?").button(" Exit ", true).button(" Back ", false).key(Keys.ENTER, true)
				.key(Keys.ESCAPE, false);
	        final TextButton button1 = new TextButton("New game", Assets.skin);
	        final Widget widget1 = new Widget();
	        final TextButton button2 = new TextButton("Continue", Assets.skin);
	        
	        buttonContinue = button2;
	        button2.setDisabled(true);
	        final TextButton button3 = new TextButton("Exit", Assets.skin);
	        table.add(button1).width(buttonWidth).height(buttonHeight);
	        table.row();
	        table.add(widget1).height(buttonHeight/2);
	        table.row();
	        table.add(button2).width(buttonWidth).height(buttonHeight);
	        table.row();
	        table.add(widget1).height(buttonHeight/2);
	        table.row();
	        table.add(button3).width(buttonWidth).height(buttonHeight);
	        
	        Assets.menuButton = new Button(Assets.skin, "button-mus");
	        Assets.menuButton.setChecked(!Settings.musicEnabled);
	        final Button sndMuteButton = new Button(Assets.skin, "button-snd");
	        sndMuteButton.setChecked(!Settings.soundEnabled);
	        Table buttonTable = new Table();
	        buttonTable.bottom().left();
	        buttonTable.add(Assets.menuButton).size(buttonHeight*0.7f);
	        buttonTable.add(sndMuteButton).size(buttonHeight*0.7f);
	        stage.addActor(buttonTable);
	        
	        Assets.menuButton.addListener(new ClickListener() {
	    		public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
	    			//super.touchDown(event, x, y, pointer, button);
	    			Assets.playSnd(Assets.buttonSound);
	    			Settings.musicEnabled = !Assets.menuButton.isChecked();
    				Assets.playMusic(Assets.menuMusic);
	        	}
	    	});	        
	        sndMuteButton.addListener(new ClickListener() {
        		public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        			//super.touchDown(event, x, y, pointer, button);
        			Settings.soundEnabled = !sndMuteButton.isChecked();
            	}
        	});
	        button1.addListener(new ClickListener() {
	        		public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
	        			//super.touchDown(event, x, y, pointer, button);
	        			appHandler.getGameObject().shuffle();
	        			button2.setDisabled(false);
	        			Assets.playSnd(Assets.buttonSound);
	        			appHandler.showGame();
	            	}
	        	});
	        button2.addListener(new ClickListener() {
        		public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        			//super.touchDown(event, x, y, pointer, button);
        			if (!button2.isDisabled()) {
        				Assets.playSnd(Assets.buttonSound);
        				appHandler.showGame();
        			}
            	}
        	});
	        button3.addListener(new ClickListener() {
	        	public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        			//super.touchDown(event, x, y, pointer, button);
	        		Assets.playSnd(Assets.buttonSound);
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
		if (Settings.musicEnabled) {
			Assets.playMusic(Assets.menuMusic);
		}
	}

	@Override
	public void hide() {
		Assets.pauseMusic();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		Assets.pauseMusic();
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		Assets.playMusic(Assets.menuMusic);
	}

}
