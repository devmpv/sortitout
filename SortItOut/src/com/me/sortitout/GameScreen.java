package com.me.sortitout;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

public class GameScreen implements Screen {

	private Skin skin;
	private Stage stage;
	private SpriteBatch batch;
	boolean accelerometer;
	Box2DDebugRenderer debugRenderer;
	private GestureHandler gestureHandler = new GestureHandler();
	private GameObject game;
	private ApplicationHandler appHandler;
	private InputMultiplexer multiplexer;
	//private ShaderProgram shader;
	private OrthographicCamera camera;
	private Dialog gameOverDialog;
	private Label label1;
	private Label label2;
	private Sprite BackSprite;
	
	public GameScreen(ApplicationHandler applicationHandler) {
		appHandler = applicationHandler;
		game = applicationHandler.getGameObject();
		create();
	}
	public void create() {	
		stage = new Stage();
	    Table gameScene = new Table();
	    Table gameControls = new Table();
	    //
	    stage.addActor(gameScene);
	    skin = new Skin(Gdx.files.internal("data/skin.json"));
	    skin.add("background", new Texture("textures/bubble1.png"));
	    gameScene.setFillParent(true);
	    gameScene.bottom();
	    final Button button1 = new Button(skin);
	    final Button button2 = new Button(skin);
	    final Button button3 = new Button(skin);
	    gameOverDialog = new Dialog("", skin, "default") {
						protected void result (Object obj) {
							if (obj.equals(true)){
								game.Shuffle();
								Gdx.input.setInputProcessor(multiplexer);
								accelerometer = true;
								label1.setVisible(true);
							} else {
								appHandler.showMenu();
							}
						}
					}.text("You win!").button("Exit", false).button("New", true).key(Keys.ENTER, true).key(Keys.ESCAPE, false);
		label1 = new Label("", skin);
		label2 = new Label("", skin);
		gameScene.add(gameControls).center();
		gameControls.add(label1).width(GameObject.BLOCK_SIZE*game.BOX_TO_WORLD).height(GameObject.BLOCK_HALF*game.BOX_TO_WORLD);
		gameControls.add(button1).width(GameObject.BLOCK_HALF*game.BOX_TO_WORLD).height(GameObject.BLOCK_HALF*game.BOX_TO_WORLD);;
		gameControls.add(button2).width(GameObject.BLOCK_HALF*game.BOX_TO_WORLD).height(GameObject.BLOCK_HALF*game.BOX_TO_WORLD);;
		gameControls.add(button3).width(GameObject.BLOCK_HALF*game.BOX_TO_WORLD).height(GameObject.BLOCK_HALF*game.BOX_TO_WORLD);;
		gameControls.add(label2).width(GameObject.BLOCK_SIZE*game.BOX_TO_WORLD).height(GameObject.BLOCK_HALF*game.BOX_TO_WORLD);
		gameScene.row();
		gameScene.add(new Image(skin.getDrawable("button-disabled"))).width(game.getScreenWidth()).height(game.getScreenWidth());
		
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
		//debugRenderer = new Box2DDebugRenderer();
		//Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.getScreenWidth(), game.getScreenHeight());
	}

	

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(220, 220, 220, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);

		//Table.drawDebug(stage);

		batch.begin();
		skin.getTiledDrawable("background").draw(batch, 0, 0, stage.getWidth(), stage.getHeight());
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
		
		label1.setText(game.getTime());
		label2.setText(String.valueOf(game.getMoves()));
		stage.act(Gdx.graphics.getDeltaTime());
		
		stage.draw();
        game.WorldStep(delta);
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
		Gdx.input.setInputProcessor(multiplexer);
		label1.setVisible(true);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
	}
	public void showDialog() {
		Gdx.input.setInputProcessor(stage);
		accelerometer = false;
		label1.setVisible(false);
		gameOverDialog.show(stage);
	}
}
