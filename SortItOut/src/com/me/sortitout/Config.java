package com.me.sortitout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Config {
	//Physics parameters
	public static final float BLOCK_SIZE = 1f; //meters
	public static final float WORLD_MAX_SPEED = BLOCK_SIZE*7f;
	public static final float GRAVITY_MUL = -3f*BLOCK_SIZE;
	public static final float BLOCK_HALF = BLOCK_SIZE/2;
	public static final float BOX_STEP=1/60f;
	public static final int BOX_VELOCITY_ITERATIONS=10;  
	public static final int BOX_POSITION_ITERATIONS=20;
	public static final float BLOCK_DENSITY = 0.5f;
	public static final float BLOCK_FRICTION = 0f;
	public static final float BLOCK_RESTITUTION = 0.01f;
	public static final float BODY_LINEAR_DAMPING = 0.1f;
	public static final boolean FIXED_ROTATION = true;
	//Physics world bounds
	public static final float startpointX = BLOCK_SIZE*0.1f;
	public static final float startpointY = BLOCK_SIZE*0.1f;
	public static final float widthInMeters = BLOCK_SIZE*4.30f;
	public static final float heightInMeters = BLOCK_SIZE*4.30f;
	
	//Strings
	public static final String TIME_FORMAT = "%s:%s";
	//Sounds
	public static final Sound blockSound = Gdx.audio.newSound(Gdx.files.internal("sounds/clack1.wav"));
	public static final Sound newPosSound = Gdx.audio.newSound(Gdx.files.internal("sounds/stuck.wav"));
	public static final Sound gameOverSound = Gdx.audio.newSound(Gdx.files.internal("sounds/tada.wav"));
	public static final Sound edgeSound = Gdx.audio.newSound(Gdx.files.internal("sounds/edge_hit.wav"));
	public static final Sound ButtonSound = Gdx.audio.newSound(Gdx.files.internal("sounds/button.wav"));
	//Music
	public static final Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/menu.mp3"));
	public static final Music gameMusic = Gdx.audio.newMusic(Gdx.files.internal("music/game.mp3"));
}
