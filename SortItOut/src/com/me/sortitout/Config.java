package com.me.sortitout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Config {
	
	private static Config inst;
	//Sound
	public Sound 	
			blockSound,
			newPosSound,
			gameOverSound,
			edgeSound,
			buttonSound;
	//Music

	//Physics parameters
	public static final float BLOCK_SIZE = 1f; //meters
	public static final float MAX_SPEED = BLOCK_SIZE*6;
	public static final float MIN_SPEED = BLOCK_SIZE;
	public static final float GRAVITY_MUL = -3f*BLOCK_SIZE;
	public static final float BLOCK_HALF = BLOCK_SIZE/2;
	public static final float BOX_STEP=1/60f;
	public static final int BOX_VELOCITY_ITERATIONS=6;  
	public static final int BOX_POSITION_ITERATIONS=8;
	public static final float BLOCK_DENSITY = 1f;
	public static final float BLOCK_FRICTION = 0f;
	public static final float BLOCK_RESTITUTION = 0f;
	public static final float BODY_LINEAR_DAMPING = 0.5f;
	public static final boolean FIXED_ROTATION = true;
	//Physics world bounds
	public static final float startpointX = BLOCK_SIZE*0.1f;
	public static final float startpointY = BLOCK_SIZE*0.1f;
	public static final float widthInMeters = BLOCK_SIZE*4.30f;
	public static final float heightInMeters = BLOCK_SIZE*4.30f;
	//Collision categories
	public static final short CATEGORY_BLOCK = 0x0001;  // 0000000000000001 in binary
	public static final short CATEGORY_SCENERY = 0x0002; // 0000000000000100 in binary
	//Collision masks
	public static final short MASK_BLOCK = -1;  
	public static final short MASK_SCENERY = -1;
	//Strings
	public static final String TIME_FORMAT = "%s:%s";

	
	private Config() {
		//Music

		//Sounds
		blockSound = Gdx.audio.newSound(Gdx.files.internal("sounds/clack1.mp3"));
		newPosSound = Gdx.audio.newSound(Gdx.files.internal("sounds/stuck.mp3"));
		gameOverSound = Gdx.audio.newSound(Gdx.files.internal("sounds/win.mp3"));
		edgeSound = Gdx.audio.newSound(Gdx.files.internal("sounds/edge_hit.mp3"));
		buttonSound = Gdx.audio.newSound(Gdx.files.internal("sounds/button.mp3"));
	}
	
	public static Config getInst()
    {
        if (inst == null)
        {
            inst = new Config();
        }
        return inst;
    }
	
	public static void dispose() {
		if (inst!=null) {
			inst.audioDispose();
			inst=null;
		}
	}
	private void audioDispose(){
		//Music
		
		//Sound
		blockSound.dispose();
		newPosSound.dispose();
		gameOverSound.dispose();
		edgeSound.dispose();
		buttonSound.dispose();
	}
}
