package net.segv11.bootunlocker;

import java.io.IOException;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class mainActivity extends Activity {

	/** For logging */
	private static final String TAG = "net.segv11.mainActivity";
	private bootLoader theBootLoader = null;

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
    	Boolean setState = false;
    	Boolean desiredState = false;
    	theBootLoader = bootLoader.makeBootLoader();
    	new AsyncBootLoader().execute(setState, desiredState);
   }

    /** Called from UI to unlock the bootloader */
    public void doUnlockBootloader(View v) {
    	Boolean setState = true;
    	Boolean desiredState = false;
    	new AsyncBootLoader().execute(setState, desiredState);
    }
    
    /** Called from UI to lock the bootloader */
    public void doLockBootloader(View v) {
    	Boolean setState = true;
    	Boolean desiredState = true;
    	new AsyncBootLoader().execute(setState, desiredState);
    }

  

    private class AsyncBootLoader extends AsyncTask<Boolean, Void, Integer> {
    	/* Ideas for the future:
    	 * 		Can we receive a broadcast intent when someone ELSE tweaks the param partition?
    	 * 		Do we want a progress indicator in case su takes a long time?
    	 *		Set text colors for lock status?
    	 */ 

    	@Override
    	protected Integer doInBackground(Boolean...booleans) {
    		Boolean setState = booleans[0];
    		Boolean desiredState = booleans[1];

    		// If this device is incompatible, return immediately.
    		if (theBootLoader == null) {
    			return R.string.stat_unknown_device;
    		}

    		// If we need to change the bootloader state, then do so.
    		if (setState) {
    	    	try {
    	    		theBootLoader.setLockState(desiredState);
    			} catch (IOException e) {
    				if (desiredState) {
    					Log.e(TAG, "Caught IOException locking: " + e);
    				} else {
    					Log.e(TAG, "Caught IOException unlocking: " + e);
    				}
    			}
    	    	
    	    	// Since we changed the bootloader lock state, we will pause for
    	    	// it to take effect.  This is OK because we are on a background
    	    	// thread.
    	    	try {
					Thread.sleep(bootLoader.delayAfterChange);
				} catch (InterruptedException e) {
					// Should not happen; if it does, we just keep going.
					e.printStackTrace();
				}
    		}
    		
    		
    		// Now query the bootloader lock state.
			int blState = theBootLoader.getLockState();
	
			if (blState == bootLoader.BL_UNLOCKED) {
				return R.string.stat_unlocked;
			} else if (blState == bootLoader.BL_LOCKED) {
				return R.string.stat_locked;
			} else {
				return R.string.stat_no_root;
			}
    	}

    	
    	/*
    	 *  We don't override onProgresUpdate, because we're not
    	 *  taking that long
    	 */
    	@Override
    	protected void onPostExecute(Integer result) {
  			TextView bootLoaderStatusText = (TextView) findViewById(R.id.bootLoaderStatusText);
  			Button lockButton = (Button) findViewById(R.id.lockButton);
  			Button unlockButton = (Button) findViewById(R.id.unlockButton);
  			TextView extendedStatus = (TextView) findViewById(R.id.extendedStatus);
  			
  			bootLoaderStatusText.setText(result);
  			if ( result.equals(R.string.stat_locked) ) { 
  				lockButton.setEnabled(true);
  				unlockButton.setEnabled(true);
  				extendedStatus.setText("");
  			} else if ( result.equals(R.string.stat_unlocked) ) {
  				lockButton.setEnabled(true);
  				unlockButton.setEnabled(true);
  				extendedStatus.setText("");
  			} else if ( result.equals(R.string.stat_unknown_device ) ) {
  				lockButton.setEnabled(false);
  				unlockButton.setEnabled(false);
  				Resources res = getResources();
  				extendedStatus.setText(String.format(
  						res.getString(R.string.extra_unknown_device),
  						android.os.Build.DEVICE));
  			} else {
  				lockButton.setEnabled(false);
  				unlockButton.setEnabled(false);
  				extendedStatus.setText("");
  			}
    	}
    }
    
   
}
  