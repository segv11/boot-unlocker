package net.segv11.bootunlocker;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class mainActivity extends Activity {

	/** For logging */
	public static final String TAG = "net.segv11.mainActivity";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    /** Called at the start of the visible lifetime */
    @Override
    public void onStart() {
    	super.onStart();
		Log.v(TAG, "handling onStart ");
    	updateUI();
   }
    
    /** Called from UI to unlock the bootloader */
    public void doUnlockBootloader(View v) {
    	try {
    		bootLoader.setLockStatus(false);
		} catch (IOException e) {
    		Log.v(TAG, "Caught IOException unlocking: " + e);
		}
    	// TODO: Wait for command to finish before updating UI.
    	updateUI();
    }
    
    /** Called from UI to lock the bootloader */
    public void doLockBootloader(View v) {
    	try {
        	bootLoader.setLockStatus(true);
		} catch (IOException e) {
    		Log.v(TAG, "Caught IOException locking: " + e);
		}

    	// TODO: Wait for command to finish before updating UI.
    	updateUI();
    }

	private void updateUI() {
	   	// TODO: clean up this code, especially all these temp vars and debugging toasts
		// Receive a broadcast in case someone ELSE tweaks bootloader
	   	// TODO: Send all these commands to private constants.
	   	
	   	/*
	   	 * TODO: Need pop-up with spinner if first time requesting su?
	   	 * 
	   	 * TODO: Should we check device prop to make sure we have maguro or toro (or toroplus?)
	   	 * TODO: Set text colors for status
	   	 */
	
		Boolean runOK;
		int uiState;
	
		if (bootLoader.checkCompatibleDevice()) {
			int blState = bootLoader.getBootloaderState();
	
			if (blState == bootLoader.BL_UNLOCKED) {
				uiState = R.string.stat_unlocked;
				runOK = true;		
			} else if (blState == bootLoader.BL_LOCKED) {
				uiState = R.string.stat_locked;
				runOK = true;			
			} else {
				uiState = R.string.stat_no_root;
				runOK = false;
			}
		} else {
			uiState = R.string.stat_unknown_device;
			runOK = false;
		}
	    	
		TextView bootLoaderStatusText = (TextView) findViewById(R.id.bootLoaderStatusText);
		Button lockButton = (Button) findViewById(R.id.lockButton);
		Button unlockButton = (Button) findViewById(R.id.unlockButton);
		bootLoaderStatusText.setText(uiState);
		lockButton.setEnabled(runOK);
		unlockButton.setEnabled(runOK);
   } 
    
  
   
   
   
}
  