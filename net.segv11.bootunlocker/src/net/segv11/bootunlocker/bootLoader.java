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
 * 		manages the interface to the bootloader for locking and unlocking
 *
 */
public class bootLoader {
	// TODO: JavaDoc
	
	/** constants describing bootloader state */
	public static final int BL_UNLOCKED = 0;
	public static final int BL_LOCKED = 1;
	public static final int BL_UNKNOWN = 2;
	
	// how long to wait after calling su to update param
	// before we update the UI:
	public static final long delayAfterChange = 150;	// 150ms

	/** For logging */
	private static final String TAG = "net.segv11.bootloader";
	
	/** Private constants for working with the lock state in the param partition
	 */
	private static final String queryCommand =
		"dd ibs=1 count=1 skip=124 if=/dev/block/platform/omap/omap_hsmmc.0/by-name/param  # query "; 
	private static final String writeCommand =
	     "dd obs=1 count=1 seek=124 of=/dev/block/platform/omap/omap_hsmmc.0/by-name/param # write ";
	
    /** checks if we know how to lock/unlock the bootloader on this device */
    public static boolean checkCompatibleDevice() {
    	Log.v(TAG, "DEVICE = " + android.os.Build.DEVICE);
    	if (android.os.Build.DEVICE.equals("maguro")) {
    		return true;      	
    	} else if (android.os.Build.DEVICE.equals("toro")) {
    		return true;
    	} else if (android.os.Build.DEVICE.equals("toroplus")) {
    		return true;
    	} else {
    		return false;
    	}
    	// TODO: Should we check android.os.Build.BOOTLOADER ?
    }

    
    /** Locks or unlocks the bootloader */
    public static void setLockState(boolean newState) throws IOException {
    	if (!checkCompatibleDevice()) {
    		return;
    	}

    	int outByte;
    	if (newState) {
    		outByte = 1;
    		Log.i(TAG, "Locking bootloader by sending " + outByte + " to " + writeCommand);
    	} else {
    		outByte = 0;
    		Log.i(TAG, "Unlocking bootloader by sending " + outByte + " to " + writeCommand);
    	}
    	
		Process p = Runtime.getRuntime().exec("su");
	    DataOutputStream w=new DataOutputStream(p.getOutputStream());
	    // BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    w.writeBytes(writeCommand+ outByte +"\n");
	    w.flush();
	    w.writeByte(outByte);
	    w.flush();
	    w.writeBytes("exit\n");
	    w.flush();
	    w.close();				
    }
 
    
    /** Finds out (from the param partition) if the bootloader is unlocked */
    public static int getLockState() {
		if (! checkCompatibleDevice()) {
			return BL_UNKNOWN;
		}
		
		try {
    		Log.v(TAG, "Getting bootloader state with " + queryCommand);
			//Process p = Runtime.getRuntime().exec(new String[]{"su", "-c", queryCommand});
			//int isLocked = p.getInputStream().read();

			
			Process p = Runtime.getRuntime().exec("su");
		    DataOutputStream w =new DataOutputStream(p.getOutputStream());
		    DataInputStream r = new DataInputStream(p.getInputStream());
		    
		    w.writeBytes(queryCommand+"\n");
		    w.flush();
		    int isLocked = r.readByte();
		    w.writeBytes("exit\n");
		    w.flush();
		    w.close();				
			
			
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
   }
   
  
}
