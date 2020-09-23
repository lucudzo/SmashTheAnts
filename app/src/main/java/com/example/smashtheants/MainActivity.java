/* Joshua Farren
    CPSC 4326
    Smash the Ants!
 */

package com.example.smashtheants;


import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    MainView v;
    int maxVolume = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Disable the title
        //requestWindowFeature (Window.FEATURE_NO_TITLE);  // use the styles.xml file to set no title bar
        // Make full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Start the view
        v = new MainView(this);
        setContentView(v);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("high_score_key", Assets.highScore);
        editor.apply();
        Assets.highScore = prefs.getInt("high_score_key", 0);


    }

    @Override
    protected void onPause () {
        if(Assets.mp != null){
            Assets.mp.pause();
            Assets.mp.release();
            Assets.mp = null;
        }
        super.onPause();
        v.pause();
    }

    @Override
    protected void onResume () {
        super.onResume();
        v.resume();
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        boolean b = prefs.getBoolean("key_music_enabled", true);
        boolean c = prefs.getBoolean("key_sound_effects_enabled", true);

        if (b == true) {
            if (Assets.mp != null) {
                Assets.mp.release();
                Assets.mp = null;
            }
            Assets.mp = MediaPlayer.create(this, R.raw.ant_game_music);

            Assets.mp.setLooping(true);
            Assets.mp.start();
            Assets.mp.setVolume(1,1);

        }
        if (c == true) {
            if (Assets.sp != null) {
                Assets.sp.release();
                Assets.sp = null;
            }
            Log.i("project logging", "onResume()");
        }

    }

}
