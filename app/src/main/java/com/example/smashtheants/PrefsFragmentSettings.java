/* Joshua Farren
    CPSC 4326
    Smash the Ants!
 */

package com.example.smashtheants;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;


public class PrefsFragmentSettings extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public PrefsFragmentSettings(){

    }
    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs_fragment_settings);
        //Preference pref = getPreferenceScreen().findPreference("high_score_key");
    }

    @Override
    public void onResume(){
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        Preference pref;
        pref = getPreferenceScreen().findPreference("high_score_key");
        String s = "" + Assets.highScore;
        pref.setSummary(s);
    }

    public void onSharedPreferenceChanged (SharedPreferences sharedPreferences, String key){



        if(key.equals("key_music_enabled")){
            boolean b = sharedPreferences.getBoolean("key_music_enabled", true);
            if(b == false){
                if(Assets.mp != null){
                    Assets.mp.setVolume(0, 0);
                }
            }
            else if(Assets.mp != null){
                Assets.mp.setVolume(1,1);
            }
        }
        if(key.equals("key_sound_effects_enabled")){
            boolean c = sharedPreferences.getBoolean("key_sound_effects_enabled", true);
            if(c == false){
                if(Assets.sp != null){
                    Assets.sp.setVolume(1,0,0);
                }
            }
            else if(Assets.sp != null){
                Assets.sp.setVolume(1,1,1);
            }
        }
    }

}
