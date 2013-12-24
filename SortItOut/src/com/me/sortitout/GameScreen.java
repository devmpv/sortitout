package com.me.sortitout;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameScreen implements Screen {

	private Skin skin;
	private Stage stage;
	private SpriteBatch batch;
	Box2DDebugRenderer debugRenderer;
	private GestureHandler gestureHandler = new GestureHandler();
	private GameObject game;
	private ApplicationHandler appHandler;
	private InputMultiplexer multiplexer;
	//private ShaderProgram shader;
	private OrthographicCamera camera;
	//private Matrix4 debugMatrix;
	private Dialog gameOverDialog;
	private Label label1;
	private Label label2;
	private Button buttonAudio;
	private Button buttonGravity;
    private Button buttonExit;
    private Music gameMusic; 
    	
	public GameScreen(ApplicationHandler applicationHandler) {
		appHandler = applicationHandler;
		game = applicationHandler.getGameObject();
		create();
	}
	public void create() {	
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("music/game.mp3"));
		gameMusic.setVolume(0.3f);
		float buttonSize = game.BLOCK_HALF_PIX + game.BLOCK_HALF_PIX/2; 
		stage = new Stage();
	    Table gameScene = new Table();
	    Table gameControls = new Table();
	    //
	    stage.addActor(gameScene);
	    skin = new Skin(Gdx.files.internal("data/skin.json"));
	    skin.getFont("normaltext").setScale(appHandler.getGameObject().getScreenWidth()/480);
	    gameScene.setFillParent(true);
	    gameScene.bottom();
	    buttonAudio = new Button(skin, "button-snd");
	    buttonGravity = new Button(skin, "button-gra");
	    buttonExit = new Button(skin, "button-exit");
	    if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)){
	    	game.setAccelerometer(true);
	    } else {	    	
	    	buttonGravity.setDisabled(true);
	    }
	    gameOverDialog = new Dialog("", skin, "default") {
						protected void result (Object obj) {
							if (obj.equals(true)){
								Config.getInst().buttonSound.play();
								game.shuffle();
								Gdx.input.setInputProcessor(multiplexer);
								label1.setVisible(true);
							} else {
								Config.getInst().buttonSound.play();
								gameOverDialog.hide();
								appHandler.showMenu();
							}
						}
					}.text("You win!").button("Exit", false).button("New", true).key(Keys.ENTER, true).key(Keys.ESCAPE, false);
		label1 = new Label("", skin);
		label1.setAlignment(Align.center);
		label2 = new Label("", skin);
		label2.setAlignment(Align.center);
		gameScene.add(gameControls).center();
		gameControls.add(label1).width(game.BLOCK_SIZE_PIX).height(buttonSize);
		gameControls.add(buttonAudio).width(buttonSize).height(buttonSize);
		gameControls.add(buttonGravity).width(buttonSize).height(buttonSize);
		gameControls.add(buttonExit).width(buttonSize).height(buttonSize);
		gameControls.add(label2).width(game.BLOCK_SIZE_PIX).height(buttonSize);
		gameScene.row();
		gameScene.add(new Widget()).height(game.BLOCK_HALF_PIX/2);
		gameScene.row();
		gameScene.add(new Image(skin.getDrawable("empty"))).width(game.getScreenWidth()).height(game.getScreenWidth());
		
		buttonAudio.addListener(new ClickListener() {
    		public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
    			//super.touchDown(event, x, y, pointer, button);
    			Config.getInst().buttonSound.play();
    			if (buttonAudio.isChecked()){
    				gameMusic.pause();
    			}else {
    				gameMusic.play();
    			}
    				
        	}
    	});
		buttonGravity.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
			//super.touchDown(event, x, y, pointer, button);
				if (!buttonGravity.isDisabled()) {
					Config.getInst().buttonSound.play();
				}
				game.setAccelerometer(!buttonGravity.isChecked());
    		}
		});
		buttonExit.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
			//super.touchDown(event, x, y, pointer, button);
				Config.getInst().buttonSound.play();
				appHandler.showMenu();
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
		gestureHandler.Init(appHandler);
		//Input processor for gesture detection

		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(new GestureDetector(gestureHandler));
		multiplexer.addProcessor(new InputHandler(appHandler));
		multiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(multiplexer);
		//Physics renderer
		debugRenderer = new Box2DDebugRenderer();
		//Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.getScreenWidth(), game.getScreenHeight());
		//Debug physics
		//debugMatrix=new Matrix4(camera.combined);
		//debugMatrix.scale(game.BOX_TO_WORLD, game.BOX_TO_WORLD, 1f);
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
		//skin.getTiledDrawable("background").draw(batch, 0, 0, stage.getWidth(), stage.getHeight());
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
		appHandler.showMenu();
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void show() {
		gameOverDialog.hide();
		Gdx.input.setInputProcessor(multiplexer);		
		if (!buttonAudio.isChecked()) {
			gameMusic.play();
		}
	}

	@Override
	public void hide() {
		gameMusic.pause();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
		skin.dispose();
		gameMusic.dispose();
	}
	public void showDialog() {
		Gdx.input.setInputProcessor(stage);
		gameOverDialog.show(stage);
	}
}
