package com.me.sortitout;

import java.util.ArrayList;
import java.util.Collections;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
	
	//Physics parameters
	static final float BLOCK_SIZE = 5f; //meters
	static final float WORLD_MAX_SPEED = 2f*BLOCK_SIZE;
	static final float BLOCK_HALF = BLOCK_SIZE/2;
	static final float BOX_STEP=1/60f;
    static final int BOX_VELOCITY_ITERATIONS=10;  
    static final int BOX_POSITION_ITERATIONS=20;
    static final float BLOCK_DENSITY = 1f;
    static final float BLOCK_FRICTION = 0f;
    static final float BLOCK_RESTITUTION = 0f;
    static final float BODY_LINEAR_DAMPING = BLOCK_SIZE;
    static final boolean FIXED_ROTATION = true;
    //Variable
    float WORLD_TO_BOX;
	float BOX_TO_WORLD;
	float BLOCK_HALF_PIX;
	float BLOCK_SIZE_PIX;
	//
	private boolean accelerometer = false;
	private boolean active = false;
	//Physics world bounds
	private float startpointX = BLOCK_SIZE*0.1f;
	private float startpointY = BLOCK_SIZE*0.1f;
	private float widthInMeters = BLOCK_SIZE*4.25f;
	private float heightInMeters = BLOCK_SIZE*4.25f;
	//Screen pixel size
	private float width;
	private float heigh;
	//Body and spite lists
	private ArrayList<Body> BlockList=new ArrayList<Body>(15);
	private ArrayList<Sprite> SpriteList=new ArrayList<Sprite>(15);
	private ArrayList<Sprite> origSpriteList;
	private ArrayList<Item> ItemList = new ArrayList<Item>(15);
	private ArrayList<Rectangle> RecList = new ArrayList<Rectangle>(15);
	private Body activeBody;
	//Sounds
	Sound blockSound = Gdx.audio.newSound(Gdx.files.internal("sounds/clack1.wav"));
	Sound newPosSound = Gdx.audio.newSound(Gdx.files.internal("sounds/stuck.wav"));
	Sound gameOverSound = Gdx.audio.newSound(Gdx.files.internal("sounds/tada.wav"));
	Sound edgeSound = Gdx.audio.newSound(Gdx.files.internal("sounds/edge_hit.wav"));
	Sound ButtonSound = Gdx.audio.newSound(Gdx.files.internal("sounds/button.wav"));
	//Resources
	public World world;
	private TextureAtlas atlas;
	private ContactHandler contactHandler;
	//Delta time accumulator
	private boolean positionChanged = false;
	private float accumulator = 0;
	private int moves = 0;
	private int gameTime = 0;
	private long startTime; 
	private ApplicationHandler appHandler;
    
	public GameObject(ApplicationHandler applicationHandler, float screenWidth, float screenHeight) {
		appHandler = applicationHandler;
		width = screenWidth;
		heigh = screenHeight;
		//Scaling to screen
		BOX_TO_WORLD = screenWidth/widthInMeters;
		WORLD_TO_BOX = 1/BOX_TO_WORLD;
		BLOCK_HALF_PIX = BLOCK_HALF*BOX_TO_WORLD;
		BLOCK_SIZE_PIX = BLOCK_SIZE*BOX_TO_WORLD;
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape polygonShape = new PolygonShape();
		Sprite sprite;
		//
		world = new World(new Vector2(0, 0), true);
		atlas = new TextureAtlas(Gdx.files.internal("data/puzzle.atlas"));
		//
		setWorldBounds();       
        polygonShape.setAsBox(BLOCK_SIZE/2, BLOCK_SIZE/2);
        fixtureDef.shape = polygonShape;
        fixtureDef.density = BLOCK_DENSITY;
        fixtureDef.friction = BLOCK_FRICTION;
        fixtureDef.restitution = BLOCK_RESTITUTION;
        
        bodyDef.type = BodyType.DynamicBody;
        Body block;
        int row, column;
        for (int i=0;i<15;i++) {
        	row = Math.abs(i/4);
        	column = i - row*4;
        	bodyDef.position.x = column*BLOCK_SIZE+startpointX+BLOCK_HALF;
        	bodyDef.position.y = (3-row)*BLOCK_SIZE+startpointY+BLOCK_HALF;
			block = world.createBody(bodyDef);
			block.createFixture(fixtureDef);
			block.setFixedRotation(FIXED_ROTATION);
		    block.setLinearDamping(BODY_LINEAR_DAMPING);
		    //Assigning sprite
		    sprite = atlas.createSprite("tx_fig_"+String.valueOf(i+1));
		    sprite.setSize(BLOCK_SIZE*BOX_TO_WORLD, BLOCK_SIZE*BOX_TO_WORLD);
			SpriteList.add(i, sprite);
			//Shity but working. Keep original central points of all rectangles
			RecList.add(i, new Rectangle(Math.round((block.getPosition().x)*this.BOX_TO_WORLD), 
					Math.round((block.getPosition().y)*this.BOX_TO_WORLD),
					1,
					1));
		    block.setUserData(new Integer(i));
		    BlockList.add(i, block);
		    //Filling ItemList with correct order
		    ItemList.add(i,new Item(sprite, i));
		}
        setActiveItem(BlockList.get(0));
        //Adding empty rectangle with index 15 at position 4:4
        RecList.add(15, new Rectangle(RecList.get(14).x+BLOCK_SIZE*BOX_TO_WORLD, RecList.get(14).y, 1,1));
        //Cloning original SpriteList
        @SuppressWarnings("unchecked")
        ArrayList<Sprite> orSpriteList = (ArrayList<Sprite>) SpriteList.clone();
        origSpriteList = orSpriteList;
        //
        contactHandler = new ContactHandler();
        contactHandler.Init(this);
        world.setContactListener(contactHandler);
        //Dispose disposable
        polygonShape.dispose();
	}
	private void setWorldBounds() {
		
		Vector2 lowerLeftCorner = new Vector2(startpointX, startpointY);
		Vector2 lowerRightCorner = new Vector2(widthInMeters-startpointX, startpointY);
		Vector2 upperLeftCorner = new Vector2(startpointX, heightInMeters-startpointY);
		Vector2 upperRightCorner = new Vector2(widthInMeters-startpointX, heightInMeters-startpointY);
		
		EdgeShape EdgeBoxShape = new EdgeShape();
		Body groundBody;
		BodyDef groundBodyDef = new BodyDef();
		//
		//groundBodyDef.position.set(0, 0);
		groundBody = world.createBody(groundBodyDef);
		
        EdgeBoxShape.set(lowerLeftCorner, lowerRightCorner);
        groundBody.createFixture(EdgeBoxShape, 0).setRestitution(BLOCK_RESTITUTION);
        EdgeBoxShape.set(lowerLeftCorner, upperLeftCorner);
        groundBody.createFixture(EdgeBoxShape, 0).setRestitution(BLOCK_RESTITUTION);
        EdgeBoxShape.set(upperLeftCorner, upperRightCorner);
        groundBody.createFixture(EdgeBoxShape, 0).setRestitution(BLOCK_RESTITUTION);
        EdgeBoxShape.set(lowerRightCorner, upperRightCorner);
        groundBody.createFixture(EdgeBoxShape, 0).setRestitution(BLOCK_RESTITUTION);
	}
	public ArrayList<Body> GetBlockList() {
		return BlockList;
	}
	
	public ArrayList<Sprite> GetSpriteList() {
		return SpriteList;
	}
	
	public int Shuffle() {
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
				SpriteList = tpmSpriteList;
				DefineSpritePositions();
				ItemPositionsChanged();
				good = true;
			}
		}
		active = true;
		startTimer();
		return ShuffleCount;
	}
	private void SetGravity () {
		if (accelerometer) {
			float x = Gdx.input.getAccelerometerX();
			float y = Gdx.input.getAccelerometerY();
			Vector2 gVec2 = new Vector2((Math.abs(x)>3f) ? x : 0, Math.abs(y)>3f ? y : 0);
			world.setGravity(gVec2.scl(-5f*BLOCK_SIZE));
		}
	}
	public void WorldStep (float delta){
		//Should be improved on heavy applications (< 60 FPS)
		if (delta >= (BOX_STEP/3)) {
			world.step(delta, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
			SetGravity();
			DefineSpritePositions();
			accumulator = 0;
		} else {
			accumulator += delta;
			if (accumulator >= BOX_STEP) {
				world.step(accumulator, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
				SetGravity();
				DefineSpritePositions();
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
		// TODO Auto-generated method stub
		activeBody = block;
	}
	public Body getActiveItem() {
		return activeBody;
	}
	public ArrayList<Item> getItemList(){
		return ItemList;
	}
	public boolean ItemPositionsChanged() {
		if ((TimeUtils.millis()-startTime)/100 < 1) {
			return positionChanged;
		}
		Rectangle rec;
		Item item;
		positionChanged  = false;
		for (int i=0; i<15; i++){
			item = ItemList.get(i);
			rec = item.sprite.getBoundingRectangle();
			//!! There are 16 rectangles! so <=15
			for (int j=0; j<=15; j++) { 			
				if (rec.contains(RecList.get(j))) {
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
	public void DefineSpritePositions() {
		Sprite sprite;
		for (Body block : BlockList) {
			sprite = SpriteList.get((Integer) block.getUserData());
			sprite.setPosition(	(block.getPosition().x-GameObject.BLOCK_HALF)*BOX_TO_WORLD, 
								(block.getPosition().y-GameObject.BLOCK_HALF)*BOX_TO_WORLD);
			
		}
	}
	private void calcTime(){
		if (gameTime<999) {
			gameTime = Math.round((TimeUtils.millis()-startTime)/1000);
		}else {
			Shuffle();
		}
	}
	public void startTimer() {
		moves = 0;
		startTime = TimeUtils.millis();
		gameTime = 0;
	}
	public int getMoves()	{ 	return moves;		}
	public String getTime() {
		
		int min = gameTime / 60;
		int sec = gameTime - min*60;
		String str = String.valueOf(min).concat(":").concat(String.valueOf(sec));
		return str;	
	}
	public void GameOver() {
		// TODO Auto-generated method stub
		gameOverSound.play();
		active = false;
		appHandler.getGameScreen().showDialog();
	}
	public void setAccelerometer(boolean a) {
		this.accelerometer = a;
		world.setGravity(new Vector2());
	}
	public boolean isActive() {
		return active;
	}
}
