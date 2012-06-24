/**
 * 
 */
package net.segv11.bootunlocker;

import java.io.IOException;

import android.util.Log;

/**
 * @description
 * 		manages the interface to the bootloader for locking and unlocking
 *
 */
public class bootLoader {
	// TODO: JavaDoc
	
	/** constants describing bootloader state */
	public static final int BL_UNLOCKED = 0;
	public static final int BL_LOCKED = 1;
	public static final int BL_UNKNOWN = 2;

	/** For logging */
	public static final String TAG = "net.segv11.bootloader";
	
	/** private constants for working with the lock state in the param partition */
	private static final String queryCommand =
		"dd ibs=1 count=1 skip=124 if=/dev/block/platform/omap/omap_hsmmc.0/by-name/param"; 
	private static final String lockCommand =
		"echo -n '\\x01' |  dd obs=1 count=1 seek=124 of=/dev/block/platform/omap/omap_hsmmc.0/by-name/param";
	private static final String unlockCommand =
    	 "echo -n '\\x00' |  dd obs=1 count=1 seek=124 of=/dev/block/platform/omap/omap_hsmmc.0/by-name/param";
	
	
    /** checks if we know how to lock/unlock the bootloader on this device */
    public static boolean checkCompatibleDevice() {
    	Log.v(TAG, "DEVICE = " + android.os.Build.DEVICE);
    	if (android.os.Build.DEVICE.equals("maguro")) {
    		return true;      	
    	} else if (android.os.Build.DEVICE.equals("toro")) {
    		return true;
    	} else {
    		return false;
    	}
    	// TODO: Should we check android.os.Build.BOOTLOADER ?
    }

    
    /** Locks or unlocks the bootloader */
    public static void setLockStatus(boolean newState) throws IOException {
    	if (!checkCompatibleDevice()) {
    		return;
    	}
    	
    	if (newState) {
    		Log.i(TAG, "Locking bootloader with " + lockCommand);
    		Runtime.getRuntime().exec(new String[]{"su", "-c", lockCommand});
    	} else {
    		Log.i(TAG, "Unlocking bootloader with " + unlockCommand);
    		Runtime.getRuntime().exec(new String[]{"su", "-c", unlockCommand});
    	}
    }
 
    
    /** Finds out (from the param partition if the bootloader is unlocked */
    public static int getBootloaderState() {
		Process p;

		if (checkCompatibleDevice()) {
			try {
	    		Log.v(TAG, "Getting bootloader state with " + queryCommand);
				p = Runtime.getRuntime().exec(new String[]{"su", "-c", queryCommand});
				int isLocked = p.getInputStream().read();
	    		Log.v(TAG, "Got lock value " + isLocked);
				if (isLocked == 1) {
					return BL_LOCKED;
				} else if (isLocked == 0)  {
					return BL_UNLOCKED;
				} else {
					return BL_UNKNOWN;
				}
			} catch (IOException e) {
	    		Log.v(TAG, "Caught IOException while querying: " + e);
				return BL_UNKNOWN;
			}
		} else {
			return BL_UNKNOWN;
		}
   }
   
  
}
