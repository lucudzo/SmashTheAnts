/* Joshua Farren

    Smash the Ants!
 */

package com.example.smashtheants;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.widget.Toast;


public class MainThread extends Thread{
    private SurfaceHolder holder;
    private Handler mHandler = new Handler();
    private Handler handler;		// required for running code in the UI thread
    private boolean isRunning = false;
    Context context;
    Paint paint;
    int touchx, touchy;	// x,y of touch event
    boolean touched;	// true if touch happened
    boolean data_initialized;
    private static final Object lock = new Object();
    boolean sound1Played;
    boolean sound2Played;
    boolean sound3Played;
    int score;
    int superBugTouchCount;

    int soundPlayedCounter = 0;
    boolean missedBug;
    boolean missedSuperBug;

    public MainThread (SurfaceHolder surfaceHolder, Context context) {
        holder = surfaceHolder;
        this.context = context;
        handler = new Handler();
        data_initialized = false;
        touched = false;
    }

    public void setRunning(boolean b) {
        isRunning = b;	// no need to synchronize this since this is the only line of code to writes this variable
    }

    // Set the touch event x,y location and flag indicating a touch has happened
    public void setXY (int x, int y) {
        synchronized (lock) {
            touchx = x;
            touchy = y;
            this.touched = true;
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            // Lock the canvas before drawing
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                // Perform drawing operations on the canvas
                render(canvas);
                // After drawing, unlock the canvas and display it
                holder.unlockCanvasAndPost (canvas);
            }
        }
    }

    // Loads graphics, etc. used in game
    private void loadData (Canvas canvas) {
        Bitmap bmp;
        int newWidth, newHeight;
        float scaleFactor;

        // Create a paint object for drawing vector graphics
        paint = new Paint();

        // Load score bar
        // N/A.. score bar built into game screen

        // Load food bar
        // N/A..food bar built into game screen image

        // Load ant1
        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.ant_left_stance);
        // Compute size of bitmap needed (suppose want width = 20% of screen width)
        newWidth = (int)(canvas.getWidth() * 0.2f);
        // What was the scaling factor to get to this?
        scaleFactor = (float)newWidth / bmp.getWidth();
        // Compute the new height
        newHeight = (int)(bmp.getHeight() * scaleFactor);
        // Scale it to a new size
        Assets.ant1 = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
        // Delete the original
        bmp = null;

        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.ant_even_stance);
        // Compute size of bitmap needed (suppose want width = 20% of screen width)
        newWidth = (int)(canvas.getWidth() * 0.2f);
        // What was the scaling factor to get to this?
        scaleFactor = (float)newWidth / bmp.getWidth();
        // Compute the new height
        newHeight = (int)(bmp.getHeight() * scaleFactor);
        // Scale it to a new size
        Assets.ant2 = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
        // Delete the original
        bmp = null;

        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.ant_right_stance);
        // Compute size of bitmap needed (suppose want width = 20% of screen width)
        newWidth = (int)(canvas.getWidth() * 0.2f);
        // What was the scaling factor to get to this?
        scaleFactor = (float)newWidth / bmp.getWidth();
        // Compute the new height
        newHeight = (int)(bmp.getHeight() * scaleFactor);
        // Scale it to a new size
        Assets.ant3 = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
        // Delete the original
        bmp = null;

        //SuperBug
        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.ant_left_stance);
        // Compute size of bitmap needed (suppose want width = 20% of screen width)
        newWidth = (int)(canvas.getWidth() * 0.3f);
        // What was the scaling factor to get to this?
        scaleFactor = (float)newWidth / bmp.getWidth();
        // Compute the new height
        newHeight = (int)(bmp.getHeight() * scaleFactor);
        // Scale it to a new size
        Assets.superBug1 = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
        // Delete the original
        bmp = null;
        //
        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.ant_right_stance);
        // Compute size of bitmap needed (suppose want width = 20% of screen width)
        newWidth = (int)(canvas.getWidth() * 0.3f);
        // What was the scaling factor to get to this?
        scaleFactor = (float)newWidth / bmp.getWidth();
        // Compute the new height
        newHeight = (int)(bmp.getHeight() * scaleFactor);
        // Scale it to a new size
        Assets.superBug2 = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
        // Delete the original
        bmp = null;
        // dead superBug
        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.dead_ant);
        // Compute size of bitmap needed (suppose want width = 20% of screen width)
        newWidth = (int)(canvas.getWidth() * 0.3f);
        // What was the scaling factor to get to this?
        scaleFactor = (float)newWidth / bmp.getWidth();
        // Compute the new height
        newHeight = (int)(bmp.getHeight() * scaleFactor);
        // Scale it to a new size
        Assets.deadSuperBug = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
        // Delete the original
        bmp = null;


        // Load roach3 (dead bug)
        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.dead_ant);
        // Compute size of bitmap needed (suppose want width = 20% of screen width)
        newWidth = (int)(canvas.getWidth() * 0.5f);
        // What was the scaling factor to get to this?
        scaleFactor = (float)newWidth / bmp.getWidth();
        // Compute the new height
        newHeight = (int)(bmp.getHeight() * scaleFactor);
        // Scale it to a new size
        Assets.deadAnt = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
        // Delete the original
        bmp = null;

        // Create a bug
        //Assets.bug = new Bug();
        for(int i = 0; i < Assets.bugs.length; i++){
            Assets.bugs[i] = new Bug();
        }


    }

    // Load specific game screen/ background screen
    private void loadBackground (Canvas canvas, int resId) {
        // Load background
        Bitmap bmp = BitmapFactory.decodeResource (context.getResources(), resId);
        // Scale it to fill entire canvas
        Assets.gameScreen= Bitmap.createScaledBitmap (bmp, canvas.getWidth(), canvas.getHeight(), false);
        // Delete the original
        bmp = null;
    }

    private void render (Canvas canvas) {
        int i, x, y;
        if(soundPlayedCounter == 3){
            soundPlayedCounter = 0;
            sound1Played = false;
            sound2Played = false;
            sound3Played = false;
        }

        if (! data_initialized) {
            loadData(canvas);
            data_initialized = true;
        }

        switch (Assets.state) {
            case GettingReady:
                loadBackground (canvas, R.drawable.game_screen);
                // Draw the background screen
                canvas.drawBitmap (Assets.gameScreen, 0, 0, null);
                // Play a sound effect
                Assets.soundPool.play(Assets.getReady, 1, 1, 1, 0, 1);
                // Start a timer
                Assets.gameTimer = System.nanoTime() / 1000000000f;
                // Go to next state
                Assets.state = Assets.GameState.Starting;
                break;
            case Starting:
                score = 0;
                // Draw the background screen
                canvas.drawBitmap (Assets.gameScreen, 0, 0, null);
                // Has 3 seconds elapsed?
                float currentTime = System.nanoTime() / 1000000000f;
                if (currentTime - Assets.gameTimer >= 3)
                    // Goto next state
                    Assets.state = Assets.GameState.Running;
                break;
            case Running:
                // Draw the background screen
                canvas.drawBitmap (Assets.gameScreen, 0, 0, null);


                // Draw one circle for each life at top right corner of screen
                // Let circle radius be 5% of width of screen
                int radius = (int)(canvas.getWidth() * 0.05f);
                int spacing = 8; // spacing in between circles
                x = canvas.getWidth() - radius - spacing;	// coordinates for rightmost circle to draw
                y = radius + spacing;

                Paint paintText = new Paint();
                paintText.setTextSize(50);
                canvas.drawText("Score: " + score, 0, 100, paintText);


                //Add pause button here

                //

                for (i=0; i<Assets.livesLeft; i++) {

                    paint.setColor(Color.BLACK);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawCircle(x, y, radius, paint);

                    paint.setColor(Color.BLACK);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawCircle(x, y, radius, paint);

                    // Reposition to draw the next circle to the left
                    x -= (radius*2 + spacing);
                }

                // Process a touch
                if (touched) {
                    for(int t = 0; t < Assets.bugs.length; t++) {

                            // Set touch flag to false since we are processing this touch now
                            touched = false;

                            // See if this touch killed a bug
                            boolean bugKilled = Assets.bugs[t].touched(canvas, touchx, touchy);

                            //for superBug killed
                            boolean superBugKilled = Assets.superBug.superTouched(canvas, touchx, touchy);


                            if (superBugKilled) {
                                Assets.soundPool.play(Assets.superBugSplat, 1, 1, 1, 0, 1);
                                score += 10;
                            }
                            if (bugKilled && !sound1Played) {
                                Assets.soundPool.play(Assets.squish1, 1, 1, 1, 0, 1);
                                sound1Played = true;
                                soundPlayedCounter++;
                                score++;

                            } else if (bugKilled && !sound2Played) {
                                Assets.soundPool.play(Assets.squish2, 1, 1, 1, 0, 1);
                                sound2Played = true;
                                soundPlayedCounter++;
                                score++;

                            } else if (bugKilled && !sound3Played) {
                                Assets.soundPool.play(Assets.squish3, 1, 1, 1, 0, 1);
                                sound3Played = true;
                                soundPlayedCounter++;
                                score++;

                            }
                            /*
                            this was meant to catch cases where screen was touched but no bug killed

                            if(!superBugKilled && !bugKilled){
                                Assets.soundPool.play(Assets.thud, 1, 1, 1, 0, 1);
                            }*/



                    }

                }

                //draw dead bugs on screen
                for(int t = 0; t < Assets.bugs.length; t++){
                    Assets.bugs[t].drawDead(canvas);
                }
                for(int t = 0; t < Assets.bugs.length; t++){
                    Assets.bugs[t].move(canvas);
                }
                for(int t = 0; t < Assets.bugs.length; t++) {
                    Assets.bugs[t].birth(canvas);
                }

                // ADD MORE CODE HERE TO PLAY GAME
                Assets.superBug.drawDead(canvas);
                Assets.superBug.move(canvas);
                Assets.superBug.birth(canvas);


                // Are no lives left?
                if (Assets.livesLeft == 0)
                    // Goto next state
                    Assets.state = Assets.GameState.GameEnding;
                break;
            case GameEnding:
                // Show a game over message
                Assets.soundPool.play(Assets.gameOver, 1, 1, 1, 0, 1);
                handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(context, "Game Over", Toast.LENGTH_SHORT).show();

                        if(score > Assets.highScore){
                            Toast.makeText(context, "New high score! : " + score, Toast.LENGTH_SHORT).show();
                                Assets.highScore= score;

                        }else {
                            Toast.makeText(context, "Your Score: " + score, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                // Goto next state


                Assets.state = Assets.GameState.GameOver;
                break;
            case GameOver:
                // Fill the entire canvas' bitmap with 'black'
                canvas.drawColor(Color.BLACK);

               // Toast.makeText(context, "Click back to return to title screen", Toast.LENGTH_LONG).show();


                break;
        }
    }
}
