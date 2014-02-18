package com.me.sortitout.screens;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.sortitout.Assets;
import com.me.sortitout.GameApp;
import com.me.sortitout.GameObject;
import com.me.sortitout.GestureHandler;
import com.me.sortitout.InputHandler;
import com.me.sortitout.Item;
import com.me.sortitout.Settings;

public class GameScreen implements Screen {

	private Stage stage;
	private SpriteBatch batch;
	private GestureHandler gestureHandler = new GestureHandler();
	private GameObject game;
	private InputMultiplexer multiplexer;
	private OrthographicCamera camera;
	//private Box2DDebugRenderer debugRenderer;
	//private Matrix4 debugMatrix;
	private Label label1;
	private Label label2;
	private Button buttonGravity;
    private Button buttonExit;
    	
	public GameScreen() {
		float buttonSize = GameObject.BLOCK_HALF_PIX + GameObject.BLOCK_HALF_PIX/2;
		game = GameApp.gameObject;
		stage = new Stage();
	    Table gameScene = new Table();
	    Table gameControls = new Table();
	    //
	    stage.addActor(gameScene);
	    gameScene.setFillParent(true);
	    gameScene.bottom();
	    buttonGravity = new Button(Assets.skin, "button-gra");
	    buttonExit = new Button(Assets.skin, "button-exit");
	    Assets.gameButton = new Button(Assets.skin, "button-mus");
	    Assets.gameButton.setChecked(!Settings.musicEnabled);

	    if (!Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)){
	    	buttonGravity.setDisabled(true);
	    }else {
	    	buttonGravity.setChecked(true);
	    }
		label1 = new Label("", Assets.skin, "gamelabel");
		label1.setAlignment(Align.center);
		label2 = new Label("", Assets.skin, "gamelabel");
		label2.setAlignment(Align.center);
        
		
		gameScene.add(gameControls).center();
		gameControls.add(label1).width(GameObject.BLOCK_SIZE_PIX).height(buttonSize);
		gameControls.add(Assets.gameButton).width(buttonSize).height(buttonSize);
		gameControls.add(buttonGravity).width(buttonSize).height(buttonSize);
		gameControls.add(buttonExit).width(buttonSize).height(buttonSize);
		gameControls.add(label2).width(GameObject.BLOCK_SIZE_PIX).height(buttonSize);
		gameScene.row();
		gameScene.add(new Widget()).height(GameObject.BLOCK_HALF_PIX/2);
		gameScene.row();
		gameScene.add(new Widget()).width(game.getScreenWidth()).height(game.getScreenWidth());
		
		Assets.gameButton.addListener(new ClickListener() {
    		public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
    			//super.touchDown(event, x, y, pointer, button);
    			Assets.playSnd(Assets.buttonSound);
    			Settings.musicEnabled = !Assets.gameButton.isChecked();
    			Assets.playMusic(Assets.gameMusic);
        	}
    	});
		buttonGravity.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
			//super.touchDown(event, x, y, pointer, button);
				if (!buttonGravity.isDisabled()) {
					Assets.playSnd(Assets.buttonSound);
				}
				game.setAccelerometer(!buttonGravity.isChecked());
    		}
		});
		buttonExit.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
			//super.touchDown(event, x, y, pointer, button);
				Assets.playSnd(Assets.buttonSound);
				GameApp.handler.setScreen(GameApp.menuScreen);
			}
		});
		
		/*ShaderProgram.pedantic = true;
		shader = new ShaderProgram(Gdx.files.internal("shaders/simple1.vert"),
				Gdx.files.internal("shaders/simple1.frag"));
		if (!shader.isCompiled()) { Gdx.app.log("Shader", shader.getLog()); } */
		
		Gdx.graphics.setVSync(true);
		batch = new SpriteBatch();
		//batch.setShader(shader);
		batch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		//Initializing main class
		gestureHandler.Init();
		//Input processor for gesture detection

		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(new GestureDetector(gestureHandler));
		multiplexer.addProcessor(new InputHandler());
		multiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(multiplexer);
		//Physics debug renderer
		//debugRenderer = new Box2DDebugRenderer();
		//Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.getScreenWidth(), game.getScreenHeight());
		//Debug physics
		//debugMatrix=new Matrix4(camera.combined);
		//debugMatrix.scale(game.BOX_TO_WORLD, game.BOX_TO_WORLD, 1f);
	}
	public void create() {	

		
	}

	

	@Override
	public void render(float delta) {
		
		game.worldStep(delta);
		if (game.isActive()) {
			label1.setText(game.getTimeString());
		}
		label2.setText(String.valueOf(game.getMoves()));
		stage.act(Gdx.graphics.getDeltaTime());
		
		Gdx.gl.glClearColor(220, 220, 220, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		
		batch.setProjectionMatrix(camera.combined);
		//Table.drawDebug(stage);
		batch.begin();
		Assets.skin.getTiledDrawable("background").draw(batch, 0, 0, stage.getWidth(), stage.getHeight());
		Assets.skin.getDrawable("empty").draw(batch, 0, 0, stage.getWidth(), stage.getWidth());
		//drawing blocks
		Item item;
		ArrayList<Item> cItemList;
		cItemList = game.getItemList();
		for (int i=0;i<15;i++) {
			item = cItemList.get(i); 
			if (item.position == i) {
				item.sprite.draw(batch,1f);
			} else {
				item.sprite.draw(batch, 0.6f);	
			}	
		}
		//FPS
		//font.draw(batch, (Float.toString(1/delta).substring(0, 4)), 100, 550);
		batch.end();
		stage.draw();
		//Physics debug
		//debugRenderer.render(game.getWorld(), debugMatrix);
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
	}

	@Override
	public void pause() {
		GameApp.handler.setScreen(GameApp.menuScreen);
		GameApp.menuScreen.pause();
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(multiplexer);		
		if (Settings.musicEnabled) {
			Assets.playMusic(Assets.gameMusic);
		}
	}

	@Override
	public void hide() {
		Assets.pauseMusic();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
	}
}
