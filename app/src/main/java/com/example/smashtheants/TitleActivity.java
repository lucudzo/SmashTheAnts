/* Joshua Farren
    CPSC 4326
    Smash the Ants!
 */

package com.example.smashtheants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class TitleActivity extends Activity implements View.OnClickListener{
    Bitmap bmp;

    protected Button highScores, startGame;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
        bmp = null;
        setContentView(R.layout.title_activity);

        highScores = (Button) findViewById(R.id.high_scores_button);
        startGame = (Button) findViewById(R.id.start_game_button);

        highScores.setOnClickListener(this);
        startGame.setOnClickListener(this);


    }



    public class MyView extends View {
        boolean switchToGameScreen;

        public MyView(Context context) {
            super(context);
            switchToGameScreen = false;

        }

        @Override
        protected void onDraw(Canvas canvas) {
            // Load image if needed
            if (bmp == null)
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.title_screen);
            // Draw the title full screen
            Rect dstRect = new Rect();
            canvas.getClipBounds(dstRect);
            canvas.drawBitmap(bmp, null, dstRect, null);



/*
            // On click switch to main (game) activity
            if (switchToGameScreen) {
                switchToGameScreen = false;
                startActivity(new Intent(TitleActivity.this, MainActivity.class));
                // Delete image (don't need it in memory if not using it)
                bmp = null;
            }
            else
                invalidate();
        }
        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            // On click set flag to switch to main (game) activity
            //if (event.getAction() == MotionEvent.ACTION_UP)
            //    switchToGameScreen = true;
            //return true; // to indicate we have handled this event
        }*/
        }

       /* public void onClick(View v) {
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            switch (v.getId()) {
                case R.id.high_scores_button:
                    Intent intent1 = new Intent(TitleActivity.this, HighScoreActivity.class);
                    startActivity(intent1);

                case R.id.start_game_button:
                    Intent intent2 = new Intent(TitleActivity.this, MainActivity.class);
                    startActivity(intent2);
            }

        }*/


    }
    @Override
    public void onClick(View v) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        switch (v.getId()) {
            case R.id.high_scores_button:
                Intent intent1 = new Intent(TitleActivity.this, PrefActivity.class);
                startActivity(intent1);
                break;

            case R.id.start_game_button:
                Intent intent2 = new Intent(TitleActivity.this, MainActivity.class);
                startActivity(intent2);
                break;
        }

    }
}
