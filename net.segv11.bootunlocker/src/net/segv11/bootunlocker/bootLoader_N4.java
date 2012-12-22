/**
 * 
 */
package net.segv11.bootunlocker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.util.Log;

/**
 * @description
 * 		device-specific bootloader code for Nexus 4 phones
 *
 */
public class bootLoader_N4 extends bootLoader {
	// TODO: JavaDoc
	
	/** For logging */
	private static final String TAG = "net.segv11.bootLoader_N4";
	
	/** Private constants for working with the lock state in the param partition
	 */
	private static final String queryCommand =
		"dd ibs=1 count=1 skip=16400 if=/dev/block/platform/msm_sdcc.1/by-name/misc  # query "; 
	private static final String writeCommand =
	     "dd obs=1 count=1 seek=16400 of=/dev/block/platform/msm_sdcc.1/by-name/misc # write ";
    
    /** Locks or unlocks the bootloader */
    @Override
    public void setLockState(boolean newState) throws IOException { 	
    	int outByte;
    	if (newState) {
    		outByte = 0;
    		Log.i(TAG, "Locking bootloader by sending " + outByte + " to " + writeCommand);
    	} else {
    		outByte = 1;
    		Log.i(TAG, "Unlocking bootloader by sending " + outByte + " to " + writeCommand);
    	}

    	superUserCommandWithDataByte(writeCommand, outByte);
    }
 
    
    /** Finds out (from the misc partition) if the bootloader is unlocked */
    @Override
    public int getLockState() {
		try {
    		Log.v(TAG, "Getting bootloader state with " + queryCommand);
    		
    		int lockResult = superUserCommandWithByteResult(queryCommand);
			
			Log.v(TAG, "Got lock value " + lockResult);
			if (lockResult == 0) {
				return BL_LOCKED;
			} else if (lockResult == 1)  {
				return BL_UNLOCKED;
			} else {
				return BL_UNKNOWN;
			}
		} catch (IOException e) {
    		Log.v(TAG, "Caught IOException while querying: " + e);
			return BL_UNKNOWN;
		}
   }  
}
