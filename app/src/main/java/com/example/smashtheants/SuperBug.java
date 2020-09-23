/* Joshua Farren
    CPSC 4326
    Smash the Ants!
 */

package com.example.smashtheants;

import android.graphics.Canvas;

public class SuperBug {

    // States of a Bug
    enum BugState {
        Dead,
        ComingBackToLife,
        Alive, 			    // in the game
        DrawDead,			// draw dead body on screen
    };

    Bug.BugState state;			// current state of bug
    int x,y;				// location on screen (in screen coordinates)
    double speed;			// speed of bug (in pixels per second)


    // All times are in seconds
    float timeToBirth;		// # seconds till birth
    float startBirthTimer;	// starting timestamp when decide to be born
    float deathTime;		// time of death
    float animateTimer;		// used to move and animate the bug
    int touchCount = 0;
    // Bug starts not alive
    public SuperBug () {
        state = Bug.BugState.Dead;
    }

    // Bug birth processing
    public void birth (Canvas canvas) {
        // Bring a bug to life?
        if (state == Bug.BugState.Dead) {
            // Set it to coming alive
            state = Bug.BugState.ComingBackToLife;
            // Set a random number of seconds before it comes to life
                timeToBirth = 20;


            // Note the current time
            startBirthTimer = System.nanoTime() / 1000000000f;
        }
        // Check if bug is alive yet
        else if (state == Bug.BugState.ComingBackToLife) {
            float curTime = System.nanoTime() / 1000000000f;
            // Has birth timer expired?
            if (curTime - startBirthTimer >= timeToBirth) {
                // If so, then bring bug to life
                state = Bug.BugState.Alive;
                // Set bug starting location at top of screen
                x = (int)(Math.random() * canvas.getWidth());
                // Keep entire bug on screen
                if (x < Assets.superBug1.getWidth()/2)
                    x = Assets.superBug1.getWidth()/2;
                else if (x > canvas.getWidth() - Assets.superBug1.getWidth()/2)
                    x = canvas.getWidth() - Assets.superBug1.getWidth()/2;
                y = 0;
                // Set speed of this bug
                speed = (canvas.getHeight() / 4) - 250; // no faster than 1/4 a screen per second
                // subtract a random amount off of this so some bugs are a little slower

                // Record timestamp of this bug being born
                animateTimer = curTime;
            }
        }
    }

    // Bug movement processing
    public void move (Canvas canvas) {

        // Make sure this bug is alive
        if (state == Bug.BugState.Alive) {
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
                canvas.drawBitmap(Assets.superBug1, x-Assets.superBug1.getWidth()/2,  y-Assets.superBug1.getHeight()/2, null);
            }
            else {
                canvas.drawBitmap(Assets.superBug2, x-Assets.superBug2.getWidth()/2,  y-Assets.superBug2.getHeight()/2, null);
            }
            // Has it reached the bottom of the screen?
            if (y >= canvas.getHeight()) {
                //play eating sounds
                Assets.soundPool.play(Assets.antsEating, 1, 1, 1, 0, 1);
                // Kill the bug
                state = Bug.BugState.Dead;
                // Subtract 1 life
                Assets.livesLeft--;
            }
        }
    }

    // Process touch to see if kills bug - return true if bug killed
    public boolean superTouched (Canvas canvas, int touchx, int touchy) {
        boolean bugTouched = false;
        // Make sure this bug is alive
        if (state == Bug.BugState.Alive) {
            // Compute distance between touch and center of bug
            float dis = (float)(Math.sqrt ((touchx - x) * (touchx - x) + (touchy - y) * (touchy - y)));
            // Is this close enough for a kill?
            if (dis <= Assets.superBug1.getWidth()*0.25f) {
                deathTime = System.nanoTime() / 1000000000f;
                state = Bug.BugState.DrawDead;
                return true;

            }
        }
        //Another attempt at catching non-kills to play thud sound..
        //Assets.soundPool.play(Assets.thud, 1, 1, 1, 0, 1);
        return bugTouched;
    }

    // Draw dead bug body on screen, if needed
    public void drawDead (Canvas canvas) {
        if (state == Bug.BugState.DrawDead) {
            canvas.drawBitmap(Assets.deadSuperBug, x-Assets.superBug1.getWidth()/2,  y-Assets.superBug1.getHeight()/2, null);
            // Get time since death
            float curTime = System.nanoTime() / 1000000000f;
            float timeSinceDeath = curTime - deathTime;
            // Drawn dead body long enough (4 seconds) ?
            if (timeSinceDeath > 4)
                state = Bug.BugState.Dead;
        }
    }

}

