package com.me.sortitout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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
	public Music
			menuMusic,
			gameMusic;
	//Physics parameters
	public static final float BLOCK_SIZE = 1f; //meters
	public static final float MAX_SPEED = BLOCK_SIZE*5;
	public static final float MIN_SPEED = BLOCK_SIZE;
	public static final float GRAVITY_MUL = -3f*BLOCK_SIZE;
	public static final float BLOCK_HALF = BLOCK_SIZE/2;
	public static final float BOX_STEP=1/60f;
	public static final int BOX_VELOCITY_ITERATIONS=10;  
	public static final int BOX_POSITION_ITERATIONS=20;
	public static final float BLOCK_DENSITY = 0.5f;
	public static final float BLOCK_FRICTION = 0f;
	public static final float BLOCK_RESTITUTION = 0f;
	public static final float BODY_LINEAR_DAMPING = 0.5f;
	public static final boolean FIXED_ROTATION = true;
	//Physics world bounds
	public static final float startpointX = BLOCK_SIZE*0.1f;
	public static final float startpointY = BLOCK_SIZE*0.1f;
	public static final float widthInMeters = BLOCK_SIZE*4.30f;
	public static final float heightInMeters = BLOCK_SIZE*4.30f;
	
	//Strings
	public static final String TIME_FORMAT = "%s:%s";

	
	private Config() {
		//Music
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/menu.mp3"));
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("music/game.mp3"));
		gameMusic.setVolume(0.3f);
		//Sounds
		blockSound = Gdx.audio.newSound(Gdx.files.internal("sounds/clack1.wav"));
		newPosSound = Gdx.audio.newSound(Gdx.files.internal("sounds/stuck.wav"));
		gameOverSound = Gdx.audio.newSound(Gdx.files.internal("sounds/tada.wav"));
		edgeSound = Gdx.audio.newSound(Gdx.files.internal("sounds/edge_hit.wav"));
		buttonSound = Gdx.audio.newSound(Gdx.files.internal("sounds/button.wav"));
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
		}
	}
	private void audioDispose(){
		//Music
		menuMusic.stop();
		menuMusic.dispose();
		gameMusic.stop();
		gameMusic.dispose();
		
		//Sound
		blockSound.dispose();
		newPosSound.dispose();
		gameOverSound.dispose();
		edgeSound.dispose();
		buttonSound.dispose();
	}
}
