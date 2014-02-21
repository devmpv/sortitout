package com.me.sortitout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	
	//Sound
	public static Sound 	
			blockSound,
			newPosSound,
			gameOverSound,
			edgeSound,
			buttonSound;
	//Music
	public static Music
			gameMusic,
			menuMusic;
	public static Button menuButton, gameButton;

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
	public static final float BLOCK_DENSITY = 1f;
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
	//Skin
	public static Skin skin;
	//UI
	public static List list;
	public static void Load() {
		//Music
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/menu.mp3"));
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("music/game.mp3"));
		gameMusic.setVolume(0.3f);
		//Sounds
		blockSound = Gdx.audio.newSound(Gdx.files.internal("sounds/clack1.mp3"));
		newPosSound = Gdx.audio.newSound(Gdx.files.internal("sounds/stuck.mp3"));
		gameOverSound = Gdx.audio.newSound(Gdx.files.internal("sounds/win.mp3"));
		edgeSound = Gdx.audio.newSound(Gdx.files.internal("sounds/edge_hit.mp3"));
		buttonSound = Gdx.audio.newSound(Gdx.files.internal("sounds/button.mp3"));
		//Skin
		skin = new Skin(Gdx.files.internal("data/skin.json"));
        skin.getFont("normaltext").setScale((float)Gdx.graphics.getWidth()/480);
        //Score list
        list = new List(Settings.highscores, skin);
	}
	
	public static void dispose() {
			audioDispose();
			skin.dispose();
	}
	public static void pauseMusic() {
		menuMusic.pause();
		gameMusic.pause();
		menuButton.setChecked(true);
		gameButton.setChecked(true);
	}
	public static void playMusic(Music music) {
		if (Settings.musicEnabled) {
			music.play();
			menuButton.setChecked(false);
			gameButton.setChecked(false);
		} else {
			pauseMusic();
		}
	}
	public static void playSnd (Sound sound) { 		playSnd(sound, 1, 1); 	}
	public static void playSnd (Sound sound, float volume) {	playSnd(sound, volume, 1); 	}
	public static void playSnd (Sound sound, float volume, float pitch) {
		if (Settings.soundEnabled) 	sound.play(volume, pitch, 0);
	}
	private static void audioDispose(){
		//Music
		gameMusic.stop();
		menuMusic.stop();
		gameMusic.dispose();
		menuMusic.dispose();
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
