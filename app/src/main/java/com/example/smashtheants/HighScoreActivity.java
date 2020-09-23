/* Joshua Farren
    CPSC 4326
    Smash the Ants!
 */
package com.example.smashtheants;

import android.app.Activity;
import android.os.Bundle;

public class HighScoreActivity extends Activity {
    public int firstPlace;
    public int secondPlace;
    public int thirdPlace;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.high_score_activity);
    }
}
