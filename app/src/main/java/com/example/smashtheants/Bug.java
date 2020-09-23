/* Joshua Farren
    CPSC 4326
    Smash the Ants!
 */

package com.example.smashtheants;

import android.graphics.Canvas;

public class Bug {
    // States of a Bug
    enum BugState {
        Dead,
        ComingBackToLife,
        Alive, 			    // in the game
        DrawDead,			// draw dead body on screen
    };

    BugState state;			// current state of bug
    int x,y;				// location on screen (in screen coordinates)
    double speed;			// speed of bug (in pixels per second)
    double slowSpeed;

    // All times are in seconds
    float timeToBirth;		// # seconds till birth
    float startBirthTimer;	// starting timestamp when decide to be born
    float deathTime;		// time of death
    float animateTimer;		// used to move and animate the bug
    int counter = 0;
    // Bug starts not alive
    public Bug () {
        state = BugState.Dead;
    }

    // Bug birth processing
    public void birth (Canvas canvas) {
        // Bring a bug to life?
        if (state == BugState.Dead) {
            // Set it to coming alive
            state = BugState.ComingBackToLife;
            // Set a random number of seconds before it comes to life
            if(counter == 0) {
                timeToBirth = (float) Math.random() * 2;
                counter++;
            }else if(counter == 1){
                timeToBirth = (float) Math.random() * 3;
                counter++;
            }else if(counter == 2){
                timeToBirth = (float) Math.random() * 4;
                counter = 0;
            }
            // Note the current time
            startBirthTimer = System.nanoTime() / 1000000000f;
        }
        // Check if bug is alive yet
        else if (state == BugState.ComingBackToLife) {
            float curTime = System.nanoTime() / 1000000000f;
            // Has birth timer expired?
            if (curTime - startBirthTimer >= timeToBirth) {
                // If so, then bring bug to life
                state = BugState.Alive;
                // Set bug starting location at top of screen
                x = (int)(Math.random() * canvas.getWidth());
                // Keep entire bug on screen
                if (x < Assets.ant1.getWidth()/2)
                    x = Assets.ant1.getWidth()/2;
                else if (x > canvas.getWidth() - Assets.ant1.getWidth()/2)
                    x = canvas.getWidth() - Assets.ant1.getWidth()/2;
                y = 0;
                // Set speed of this bug
                speed = canvas.getHeight() / 4 - (Math.random()*350); // no faster than 1/4 a screen per second
                // subtract a random amount off of this so some bugs are a little slower
                // ADD CODE HERE
                slowSpeed = canvas.getHeight() / 4 - 500;
                // Record timestamp of this bug being born
                animateTimer = curTime;
            }
        }
    }

    // Bug movement processing
    public void move (Canvas canvas) {

        // Make sure this bug is alive
        if (state == BugState.Alive) {
            // Get elapsed time since last call here
            float curTime = System.nanoTime() / 1000000000f;
            float elapsedTime = curTime - animateTimer;
            animateTimer = curTime;
            // Compute the amount of pixels to move (vertically down the screen)

            //every third bug will move at slower speed

                    y += (speed * elapsedTime);


            // Draw bug on screen
            // ADD CODE HERE - Draw each frame of animation as appropriate - don't just draw 1 frame

            long newCurTime = System.currentTimeMillis() / 100 % 10;
            if (newCurTime % 2 == 0) {
                canvas.drawBitmap(Assets.ant1, x-Assets.ant1.getWidth()/2,  y-Assets.ant1.getHeight()/2, null);
            }
            else {
                canvas.drawBitmap(Assets.ant3, x-Assets.ant1.getWidth()/2,  y-Assets.ant1.getHeight()/2, null);
            }
            // Has it reached the bottom of the screen?
            if (y >= canvas.getHeight()) {
                //play eating sounds
                Assets.soundPool.play(Assets.antsEating, 1, 1, 1, 0, 1);
                // Kill the bug
                state = BugState.Dead;
                // Subtract 1 life
                Assets.livesLeft--;
            }
        }
    }

    // Process touch to see if kills bug - return true if bug killed
    public boolean touched (Canvas canvas, int touchx, int touchy) {
        boolean touched = false;
        // Make sure this bug is alive
        if (state == BugState.Alive) {
            // Compute distance between touch and center of bug
            float dis = (float)(Math.sqrt ((touchx - x) * (touchx - x) + (touchy - y) * (touchy - y)));
            // Is this close enough for a kill?
            if (dis <= Assets.ant1.getWidth()*0.75f) {
                state = BugState.DrawDead;	// need to draw dead body on screen for a while
                touched = true;
                // Record time of death
                deathTime = System.nanoTime() / 1000000000f;

            }
        }
        //Another attempt at trying to catch non-kills to play thud sound
        //Assets.soundPool.play(Assets.thud, 1, 1, 1, 0, 1);
        return (touched);
    }

    // Draw dead bug body on screen, if needed
    public void drawDead (Canvas canvas) {
        if (state == BugState.DrawDead) {
            canvas.drawBitmap(Assets.deadAnt, x-Assets.ant1.getWidth()/2,  y-Assets.ant1.getHeight()/2, null);
            // Get time since death
            float curTime = System.nanoTime() / 1000000000f;
            float timeSinceDeath = curTime - deathTime;
            // Drawn dead body long enough (4 seconds) ?
            if (timeSinceDeath > 4)
                state = BugState.Dead;
        }
    }

}
