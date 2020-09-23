/* Joshua Farren
    CPSC 4326
    Smash the Ants!
 */

package com.example.smashtheants;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.widget.Button;

public class Assets {
    static Bitmap gameScreen;
    static Bitmap ant1;
    static Bitmap ant2;
    static Bitmap ant3;
    static Bitmap deadAnt;

    static Bitmap superBug1;
    static Bitmap superBug2;
    static Bitmap deadSuperBug;

    public static int highScore;

    public static MediaPlayer mp;
    public static SoundPool sp;

    // States of the Game Screen
    enum GameState {
        GettingReady,	// play "get ready" sound and start timer, goto next state
        Starting,		// when 3 seconds have elapsed, goto next state
        Running, 		// play the game, when livesLeft == 0 goto next state
        GameEnding,	    // show game over message
        GameOver,		// game is over, wait for any Touch and go back to title activity screen
    };
    static GameState state;		// current state of the game
    static float gameTimer;	    // in seconds
    static int livesLeft;		// 0-3

    static SoundPool soundPool;
    static int getReady;
    static int squish1;
    static int squish2;
    static int squish3;
    static int thud;
    static int gameOver;
    static int antsEating;
    static int superBugSplat;


    static Bug [] bugs = new Bug[6];

    static SuperBug superBug = new SuperBug();




}
