package com.me.sortitout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class Config {
	
	private static Config inst;
	//Sound
	private Sound 	
			blockSound,
			newPosSound,
			gameOverSound,
			edgeSound,
			buttonSound;
	//Sound IDs
	public static final int 
		SND_BLOCK = 1,
		SND_NEWPOS = 2,
		SND_GAMEOVER = 3,
		SND_EDGE = 4,
		SND_BUTTON = 5;
	//Music
	public Music
			gameMusic,
			menuMusic;
	public Button menuButton, gameButton;

	public static Boolean mute = false;
	//Physics parameters
	public static final float[] vertices = {
			-0.35f, -0.5f,
			-0.5f, -0.35f,
			-0.5f, 0.35f,
			-0.35f, 0.5f,
			0.35f, 0.5f,
			0.5f, 0.35f,
			0.5f, -0.35f,
			0.35f, -0.5f};
	public static final float BLOCK_SIZE = 1f; //meters
	public static final float MAX_SPEED = BLOCK_SIZE*6;
	public static final float MIN_SPEED = BLOCK_SIZE;
	public static final float GRAVITY_MUL = -3f*BLOCK_SIZE;
	public static final float BLOCK_HALF = BLOCK_SIZE/2;
	public static final float BOX_STEP=1/60f;
	public static final int BOX_VELOCITY_ITERATIONS=15;  
	public static final int BOX_POSITION_ITERATIONS=8;
	public static final float BLOCK_DENSITY = 0.1f;
	public static final float BLOCK_FRICTION = 0f;
	public static final float BLOCK_RESTITUTION = 0f;
	public static final float BODY_LINEAR_DAMPING = 0.9f;
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
		//Button
	}
	
	public static Config getInst()
    {
        if (inst == null) {
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
	public void pauseMusic() {
		menuMusic.pause();
		gameMusic.pause();
		menuButton.setChecked(true);
		gameButton.setChecked(true);
	}
	public void playMusic(Music music) {
		music.play();
		menuButton.setChecked(false);
		gameButton.setChecked(false);
	}
	public void playSnd (int sndID) {
		this.playSnd(sndID, 1, 1);
	}
	public void playSnd (int sndID, float volume) {
		this.playSnd(sndID, volume, 1);
	}
	public void playSnd (int sndID, float volume, float pitch) {
		if (!mute) {
			switch (sndID) {
				case SND_BLOCK: blockSound.play(volume, pitch, 0); break;
				case SND_BUTTON: buttonSound.play(volume, pitch, 0); break;
				case SND_NEWPOS: newPosSound.play(volume, pitch, 0); break;
				case SND_EDGE: edgeSound.play(volume, pitch, 0); break;
				case SND_GAMEOVER: gameOverSound.play(volume, pitch, 0);
			}
		}
	}
	private void audioDispose(){
		//Music
		gameMusic = null;
		menuMusic = null;
		//Sound
		blockSound.dispose();
		newPosSound.dispose();
		gameOverSound.dispose();
		edgeSound.dispose();
		buttonSound.dispose();
	}

}
