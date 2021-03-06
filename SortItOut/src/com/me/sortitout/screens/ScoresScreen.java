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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.sortitout.Assets;
import com.me.sortitout.GameApp;
import com.me.sortitout.GameObject;
import com.me.sortitout.Settings;

public class ScoresScreen implements Screen {

	public Stage stage;
	private Label label1;
	public Dialog nameDialog;
	
	public ScoresScreen() {
		float buttonSize=GameObject.BLOCK_HALF_PIX+GameObject.BLOCK_HALF_PIX/2;
		float itemSize=GameObject.BLOCK_HALF_PIX;
		stage = new Stage();
		label1 =new Label("Your score: ", Assets.skin, "default");
		Button okButton = new Button(Assets.skin, "button-ok");		
		Widget widget = new Widget();
        Table scoresTable = new Table();
        Table table1 = new Table();
        
        table1.setFillParent(true);
        
        table1.setBackground(Assets.skin.getTiledDrawable("background"));
        table1.add(scoresTable).row();
        //scoresTable.setFillParent(true);
        scoresTable.setBackground(Assets.skin.getDrawable("empty"));
		final Button buttonExit = new Button(Assets.skin, "button-exit");
		final TextField textField = new TextField(Settings.name, Assets.skin);
		textField.setMaxLength(10);
		scoresTable.add(new Label("High scores", Assets.skin, "default")).row();
		scoresTable.add(widget).height(itemSize/2).row();
		scoresTable.add(Assets.list).row();
        scoresTable.add(widget).height(itemSize).row();
        scoresTable.add(label1).height(itemSize/2).row();
        scoresTable.add(widget).height(itemSize/2);
        table1.add(widget).height(itemSize).row();
        table1.add(buttonExit).size(buttonSize);
        
        stage.addActor(table1);
        nameDialog = new Dialog("", Assets.skin, "dialog");
        nameDialog.getContentTable().add("New score!").row();
        nameDialog.getContentTable().add(textField).height(itemSize).width(buttonSize*3);
        nameDialog.getButtonTable().add(okButton).size(buttonSize);
        //nameDialog.button(okButton, true);
        okButton.addListener(new ClickListener() {
    		public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
    			//super.touchDown(event, x, y, pointer, button);
    			Assets.playSnd(Assets.buttonSound);
    			Settings.name = textField.getText();
    			Settings.addScore(Settings.name, GameApp.gameObject.getMoves());
    			getScoreList();
    			textField.getOnscreenKeyboard().show(false);
    			nameDialog.hide();
        	}
    	});
        buttonExit.addListener(new ClickListener() {
    		public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
    			//super.touchDown(event, x, y, pointer, button);
    			Assets.playSnd(Assets.buttonSound);
    			GameApp.handler.setScreen(GameApp.menuScreen);
        	}
    	});	            
        stage.addListener(new InputListener() {
        	public boolean keyUp (InputEvent event, int keycode) {
        		if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
        			GameApp.handler.setScreen(GameApp.menuScreen);
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
		label1.setText("Your score: "+GameApp.gameObject.getMoves());
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        //Table.drawDebug(stage); // Enables debug lines for tables.
	}

	@Override
	public void show() {
		getScoreList();
		Gdx.input.setInputProcessor(stage);
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
	private void getScoreList() {
		String[] list = new String[5];
		for (int i = 0; i < 5; i++) {
			list[i] = Integer.toString(Settings.highscores[i])
					.concat("   ")
					.concat(Settings.scorenames[i]);
		}
		Assets.list.setItems(list);
	}
}
