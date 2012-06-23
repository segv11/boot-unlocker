package net.segv11.bootunlocker;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class mainActivity extends Activity {
	
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
    	updateUI();
   }
    
    /** Called from UI to unlock the bootloader */
    public void doUnlockBootloader(View v) {
    	setLockStatus(false);
    	updateUI();
    }
    
    /** Called from UI to lock the bootloader */
    public void doLockBootloader(View v) {
    	setLockStatus(true);
    	updateUI();
    }


    private void setLockStatus(boolean newState) {
    	String command;
    	if (newState) {
    		command = "echo -n '\\x01' |  dd obs=1 count=1 seek=124 of=/dev/block/platform/omap/omap_hsmmc.0/by-name/param";
    	} else {
    		command = "echo -n '\\x00' | dd obs=1 count=1 seek=124 of=/dev/block/platform/omap/omap_hsmmc.0/by-name/param";
    	}

    	try {
			Runtime.getRuntime().exec(new String[]{"su", "-c", command});
		} catch (IOException e) {
			// TODO Auto-generated catch block
	    	Toast err = Toast.makeText(this, "Cannot run SU", Toast.LENGTH_SHORT);
	    	err.show();
		}
    	// TODO: Wait for command to finish before updating UI.
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
    	 * TODO: Generate keys.  Verify sideloading works.  Think about Play Store.
    	 */
    	String command = "dd ibs=1 count=1 skip=124 if=/dev/block/platform/omap/omap_hsmmc.0/by-name/param"; 
		int theState;
		Process p;
		Boolean rootOK;

    	try {
			p = Runtime.getRuntime().exec(new String[]{"su", "-c", command});
			int isLocked = p.getInputStream().read();
			if (isLocked == 1) {
				theState = R.string.stat_locked;
				rootOK = true;
			} else if (isLocked == 0)  {
				theState = R.string.stat_unlocked;
				rootOK = true;
			} else {
				theState = R.string.stat_no_root;
				rootOK = false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			theState = R.string.stat_no_root;
			rootOK = false;
		}
    	
		TextView bootLoaderStatusText = (TextView) findViewById(R.id.bootLoaderStatusText);
		Button lockButton = (Button) findViewById(R.id.lockButton);
		Button unlockButton = (Button) findViewById(R.id.unlockButton);
		bootLoaderStatusText.setText(theState);
		lockButton.setEnabled(rootOK);
		unlockButton.setEnabled(rootOK);
	}
    
    
}