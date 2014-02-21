package com.me.sortitout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.badlogic.gdx.Gdx;

public class Settings {
        public static boolean soundEnabled = true;
        public static boolean musicEnabled = true;
        public final static Integer[] highscores = new Integer[] {100, 200, 300, 400, 500};
        public final static String[] scorenames = new String[] {"Cartman", "Kyle", "Kenny", "Stan", "Butters"};
        public final static String file = ".sortitout";
        public static String name = "Player";
        private static String version = "0.21";

        public static void load () {
                BufferedReader in = null;
                try {
                	in = new BufferedReader(new InputStreamReader(Gdx.files.local(file).read()));
                	if (version.equals(in.readLine())) {
                		name = in.readLine();
                        soundEnabled = Boolean.parseBoolean(in.readLine());
                        musicEnabled = Boolean.parseBoolean(in.readLine());
                        for (int i = 0; i < 5; i++) {
                                highscores[i] = Integer.parseInt(in.readLine());
                                scorenames[i] = in.readLine();
                        }
                    }
                } catch (Throwable e) {
                        // :( It's ok we have defaults
                } finally {
                        try {
                                if (in != null) in.close();
                        } catch (IOException e) {
                        }
                }
        }

        public static void save () {
                BufferedWriter out = null;
                try {
                        out = new BufferedWriter(new OutputStreamWriter(Gdx.files.local(file).write(false)));
                        out.write(version.concat("\n"));
                        out.write(name.concat("\n"));
                        out.write(Boolean.toString(soundEnabled).concat("\n"));
                        out.write(Boolean.toString(musicEnabled).concat("\n"));
                        for (int i = 0; i < 5; i++) {
                                out.write(Integer.toString(highscores[i]).concat("\n"));
                                out.write(scorenames[i].concat("\n"));
                        }

                } catch (Throwable e) {
                } finally {
                        try {
                                if (out != null) out.close();
                        } catch (IOException e) {
                        }
                }
        }

        public static void addScore (String name, int score) {
                for (int i = 0; i < 5; i++) {
                        if (highscores[i] > score) {
                                for (int j = 4; j > i; j--) {
                                        highscores[j] = highscores[j - 1];
                                        scorenames[j] = scorenames[j - 1];
                                }
                                highscores[i] = score;
                                scorenames[i] = name;
                                break;
                        }
                }
        }
}