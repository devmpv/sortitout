package com.me.sortitout;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.TimeUtils;

public class GameObject {
	
    //Variable
    public static float WORLD_TO_BOX;
	public static float BOX_TO_WORLD;
	public static float BLOCK_HALF_PIX;
	public static float BLOCK_SIZE_PIX;
	//
	private boolean accelerometer = false;
	private boolean active = false;
	//Screen pixel size
	private float width;
	private float heigh;
	//Body and spite lists
	private ArrayList<Body> blockList=new ArrayList<Body>(15);
	private ArrayList<Sprite> spriteList=new ArrayList<Sprite>(15);
	private ArrayList<Sprite> origSpriteList;
	private ArrayList<Item> itemList = new ArrayList<Item>(15);
	private ArrayList<Rectangle> recList = new ArrayList<Rectangle>(15);
	private Body activeBody;

	//Resources
	private World world;
	private TextureAtlas atlas;
	private ContactHandler contactHandler;
	//Delta time accumulator
	private boolean positionChanged = false;
	private float accumulator = 0;
	private int moves = 0;
	private int gameTime = 0;
	private long startTime; 
	private Vector2 noGravity = new Vector2();
	private Vector2 varGravity = new Vector2();
    
	public GameObject(float screenWidth, float screenHeight) {
		
		width = screenWidth;
		heigh = screenHeight;
		//Scaling to screen
		BOX_TO_WORLD = screenWidth/Assets.widthInMeters;
		WORLD_TO_BOX = 1/BOX_TO_WORLD;
		BLOCK_HALF_PIX = Assets.BLOCK_HALF*BOX_TO_WORLD;
		BLOCK_SIZE_PIX = Assets.BLOCK_SIZE*BOX_TO_WORLD;
		
		PolygonShape preciseShape = new PolygonShape();	
		preciseShape.set(Assets.vertices);
		Sprite sprite;
		//
		world = new World(new Vector2(0, 0), true);
		atlas = new TextureAtlas(Gdx.files.internal("data/puzzle.atlas"));
		//
		setWorldBounds();
		//Defining blocks
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
        
        fixtureDef.filter.categoryBits = Assets.CATEGORY_BLOCK;
        fixtureDef.shape = preciseShape;
        fixtureDef.density = Assets.BLOCK_DENSITY;
        fixtureDef.friction = Assets.BLOCK_FRICTION;
        fixtureDef.restitution = Assets.BLOCK_RESTITUTION;
        
        bodyDef.type = BodyType.DynamicBody;
        Body block;
        int row, column;
        for (int i=0;i<15;i++) {
        	row = Math.abs(i/4);
        	column = i - row*4;
        	bodyDef.position.x = column*Assets.BLOCK_SIZE+Assets.startpointX+Assets.BLOCK_HALF;
        	bodyDef.position.y = (3-row)*Assets.BLOCK_SIZE+Assets.startpointY+Assets.BLOCK_HALF;
			block = world.createBody(bodyDef);
			block.createFixture(fixtureDef);
			block.setFixedRotation(Assets.FIXED_ROTATION);
		    block.setLinearDamping(Assets.BODY_LINEAR_DAMPING);
		    //Assigning sprite
		    sprite = atlas.createSprite("tx_fig_"+String.valueOf(i+1));
		    sprite.setSize(Assets.BLOCK_SIZE*BOX_TO_WORLD, Assets.BLOCK_SIZE*BOX_TO_WORLD);
			spriteList.add(i, sprite);
			//Shit but it works. Keep original central points of all rectangles
			recList.add(i, new Rectangle(Math.round((block.getPosition().x)*BOX_TO_WORLD), 
					Math.round((block.getPosition().y)*BOX_TO_WORLD),
					1,
					1));
		    block.setUserData(Integer.valueOf(i));
		    blockList.add(i, block);
		    //Filling ItemList with correct order
		    itemList.add(i,new Item(sprite, i));
		}
        setActiveItem(blockList.get(0));
        //Adding empty rectangle with index 15 at position 4:4
        recList.add(15, new Rectangle(recList.get(14).x+Assets.BLOCK_SIZE*BOX_TO_WORLD, recList.get(14).y, 1,1));
        //Cloning original SpriteList
        @SuppressWarnings("unchecked")
        ArrayList<Sprite> orSpriteList = (ArrayList<Sprite>) spriteList.clone();
        origSpriteList = orSpriteList;
        //
        contactHandler = new ContactHandler();
        contactHandler.init(this);
        world.setContactListener(contactHandler);
        //Dispose disposable
        preciseShape.dispose();
	}
	private void setWorldBounds() {
		
		Vector2 lowerLeftCorner = new Vector2(Assets.startpointX, Assets.startpointY);
		Vector2 lowerRightCorner = new Vector2(Assets.widthInMeters-Assets.startpointX, Assets.startpointY);
		Vector2 upperLeftCorner = new Vector2(Assets.startpointX, Assets.heightInMeters-Assets.startpointY);
		Vector2 upperRightCorner = new Vector2(Assets.widthInMeters-Assets.startpointX, Assets.heightInMeters-Assets.startpointY);
		
		EdgeShape edgeBoxShape = new EdgeShape();
		Body groundBody;
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyType.StaticBody;
		FixtureDef groundFixtureDef = new FixtureDef();
		groundFixtureDef.shape = edgeBoxShape;
		groundFixtureDef.density = 0f;
		groundFixtureDef.filter.categoryBits = Assets.CATEGORY_SCENERY;
		groundFixtureDef.restitution = 0f;
		groundBody = world.createBody(groundBodyDef);		
        edgeBoxShape.set(lowerLeftCorner, lowerRightCorner);
        groundBody.createFixture(groundFixtureDef);
        edgeBoxShape.set(lowerLeftCorner, upperLeftCorner);
        groundBody.createFixture(groundFixtureDef);
        edgeBoxShape.set(upperLeftCorner, upperRightCorner);
        groundBody.createFixture(groundFixtureDef);
        edgeBoxShape.set(lowerRightCorner, upperRightCorner);
        groundBody.createFixture(groundFixtureDef);
        //Dispose
        edgeBoxShape.dispose();
	}
	public ArrayList<Body> getBlockList() {
		return blockList;
	}
	
	public ArrayList<Sprite> getSpriteList() {
		return spriteList;
	}
	
	public int shuffle() {
		ArrayList<Integer> tmpList = new ArrayList<Integer>();
		for (int i=0; i<15; i++) {
			tmpList.add(i);
		}
		boolean good = false;
		int ShuffleCount=0;
		while ((!good)) {
			Collections.shuffle(tmpList);
			ShuffleCount++;
			@SuppressWarnings("unchecked")
			ArrayList<Integer> clone = (ArrayList<Integer>)tmpList.clone();
			int ReplaceCount = 0;
			for (int i=0;i<15;i++) {
				for (int j=i;j<15;j++) {
					if (tmpList.get(j).equals(i)&&(i!=j)) {
						Collections.swap(tmpList, i, j);
						ReplaceCount++;
					}
				}
			}
			if ((ReplaceCount % 2)==0) {
				ArrayList<Sprite> tpmSpriteList = new ArrayList<Sprite>(15);
				for (int i=0; i<15; i++) {
					tpmSpriteList.add(i, origSpriteList.get(clone.get(i)));
				}
				spriteList = tpmSpriteList;
				defineSpritePositions();
				itemPositionsChanged();
				good = true;
			}
		}
		active = true;
		startTimer();
		return ShuffleCount;
	}
	private void setGravity () {
		if (accelerometer && active) {
			float x = Gdx.input.getAccelerometerX();
			float y = Gdx.input.getAccelerometerY();
			varGravity.set((Math.abs(x)>4f) ? x : 0, Math.abs(y)>4f ? y : 0).scl(Assets.GRAVITY_MUL);
			world.setGravity(varGravity);
		}
	}
	public void worldStep (float delta){
		//Should be improved on heavy applications (< 60 FPS)
		if (delta >= (Assets.BOX_STEP/3)) {
			world.step(delta, Assets.BOX_VELOCITY_ITERATIONS, Assets.BOX_POSITION_ITERATIONS);
			setGravity();
			defineSpritePositions();
			accumulator = 0;
		} else {
			accumulator += delta;
			if (accumulator >= Assets.BOX_STEP) {
				world.step(accumulator, Assets.BOX_VELOCITY_ITERATIONS, Assets.BOX_POSITION_ITERATIONS);
				setGravity();
				defineSpritePositions();
				accumulator = 0;
			}
		}
		calcTime();
	}
	public float getScreenWidth(){
		return width;
	}
	public float getScreenHeight(){
		return heigh;
	}
	public void setActiveItem(Body block) {
		activeBody = block;
	}
	public Body getActiveItem() {
		return activeBody;
	}
	public ArrayList<Item> getItemList(){
		return itemList;
	}
	public boolean itemPositionsChanged() {
		if ((TimeUtils.millis()-startTime) - gameTime*1000 < 100) {
			return false;
		}
		Rectangle rec;
		Item item;
		positionChanged  = false;
		for (int i=0; i<15; i++){
			item = itemList.get(i);
			rec = item.sprite.getBoundingRectangle();
			for (int j=0; j<=15; j++) { 			
				if (rec.contains(recList.get(j))) {
					if (item.position!=j) {
						item.position=j;
						moves++;
						positionChanged = true;
					}
					break;
				}
			}
		}
		return positionChanged;
	}
	public void defineSpritePositions() {
		Sprite sprite;
		for (Body block : blockList) {
			sprite = spriteList.get((Integer) block.getUserData());
			sprite.setPosition(	(block.getPosition().x-Assets.BLOCK_HALF)*BOX_TO_WORLD, 
								(block.getPosition().y-Assets.BLOCK_HALF)*BOX_TO_WORLD);
			
		}
	}
	private void calcTime(){
		if (gameTime<999) {
			gameTime = Math.round((TimeUtils.millis()-startTime)/1000);
		}else {
			shuffle();
		}
	}
	public void startTimer() {
		moves = 0;
		startTime = TimeUtils.millis();
		gameTime = 0;
	}
	public int getMoves()	{ 	
		return moves;
	}
	public String getTimeString() {
		int min = gameTime / 60;
		int sec = gameTime - min*60;
		return String.format(Assets.TIME_FORMAT, min, sec);	
	}
	public void gameOver() {
		Assets.playSnd(Assets.gameOverSound);
		active = false;
		
		GameApp.scoresScreen.nameDialog.show(GameApp.scoresScreen.stage);
		GameApp.handler.setScreen(GameApp.scoresScreen);
	}
	public void setAccelerometer(boolean a) {
		if (accelerometer != a) {
			world.setGravity(noGravity);
			for (Body body : blockList) {
				body.setAwake(true);
			}
			this.accelerometer = a;
		}
	}
	public boolean isActive() {
		return active;
	}
	public World getWorld() {
		return world;
	}
	public void dispose() {
		world.dispose();
		atlas.dispose();
	}
}
