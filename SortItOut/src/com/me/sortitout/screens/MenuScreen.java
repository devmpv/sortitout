package com.me.sortitout.screens;

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
import com.esotericsoftware.tablelayout.Cell;
import com.me.sortitout.Assets;
import com.me.sortitout.GameApp;
import com.me.sortitout.GameObject;
import com.me.sortitout.Settings;

public class MenuScreen implements Screen {

	private Stage stage;
	private Dialog exitDialog;
	private TextButton continueButton;
	
	public MenuScreen() {

		float buttonWidth=GameObject.BLOCK_SIZE_PIX*2;
		float buttonHeight=GameObject.BLOCK_HALF_PIX + GameObject.BLOCK_HALF_PIX/2;
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
			}.text("Are you sure?")
			.button(new Button(Assets.skin, "button-ok"), true)
			.button(new Button(Assets.skin, "button-cancel"), false)
			.key(Keys.ENTER, true)
			.key(Keys.ESCAPE, false);
		for ( Cell<?> cell : exitDialog.getButtonTable().getCells()) {
			cell.size(buttonHeight);
		} 
        final TextButton newGameButton = new TextButton("New game", Assets.skin);
        final Widget widget1 = new Widget();
        continueButton = new TextButton("Continue", Assets.skin);
        continueButton.setDisabled(true);
        final TextButton exitButton = new TextButton("Exit", Assets.skin);
        final TextButton scoresButton = new TextButton("Scores", Assets.skin);
        table.add(newGameButton).width(buttonWidth).height(buttonHeight).row();
        table.add(widget1).height(buttonHeight/2).row();
        table.add(continueButton).width(buttonWidth).height(buttonHeight).row();
        table.add(widget1).height(buttonHeight/2).row();
        table.add(scoresButton).width(buttonWidth).height(buttonHeight).row();
        table.add(widget1).height(buttonHeight/2).row();
        table.add(exitButton).width(buttonWidth).height(buttonHeight);
        
        Assets.menuButton = new Button(Assets.skin, "button-mus");
        Assets.menuButton.setChecked(!Settings.musicEnabled);
        final Button sndMuteButton = new Button(Assets.skin, "button-snd");
        sndMuteButton.setChecked(!Settings.soundEnabled);
        Table buttonTable = new Table();
        buttonTable.bottom().left();
        buttonTable.add(Assets.menuButton).size(buttonHeight);
        buttonTable.add(sndMuteButton).size(buttonHeight);
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
        newGameButton.addListener(new ClickListener() {
        		public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        			//super.touchDown(event, x, y, pointer, button);
        			GameApp.gameObject.shuffle();
        			continueButton.setDisabled(false);
        			Assets.playSnd(Assets.buttonSound);
        			GameApp.handler.setScreen(GameApp.gameScreen);
            	}
        	});
        continueButton.addListener(new ClickListener() {
    		public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
    			//super.touchDown(event, x, y, pointer, button);
    			if (!continueButton.isDisabled()) {
    				Assets.playSnd(Assets.buttonSound);
    				GameApp.handler.setScreen(GameApp.gameScreen);
    			}
        	}
    	});
        scoresButton.addListener(new ClickListener() {
        	public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
    			//super.touchDown(event, x, y, pointer, button);
        		Assets.playSnd(Assets.buttonSound);
        		GameApp.handler.setScreen(GameApp.scoresScreen);
        	}
    	});
        exitButton.addListener(new ClickListener() {
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

	public void create () {
			
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
		continueButton.setDisabled(!GameApp.gameObject.isActive());
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
		Assets.pauseMusic();
	}

	@Override
	public void resume() {
		Assets.playMusic(Assets.menuMusic);
	}

}
