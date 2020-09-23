/* Joshua Farren
    CPSC 4326
    Smash the Ants!
 */

package com.example.smashtheants;

import android.preference.PreferenceActivity;

import java.util.List;

public class PrefActivity extends PreferenceActivity {
    @Override
    protected boolean isValidFragment(String fragmentName){
        /*if(Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB){
            return true;
        }
        else */
        if(PrefsFragmentSettings.class.getName().equals(fragmentName)){
            return true;
        }else{
            return false;
        }
    }
    @Override
    public void onBuildHeaders(List<Header> target){
        //for elaborate, multi fragment setting screens, use this:
       // loadHeadersFromResource(R.xml.prefs_headers, target);

        //use this for single pref screens
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefsFragmentSettings()).commit();
    }

}
