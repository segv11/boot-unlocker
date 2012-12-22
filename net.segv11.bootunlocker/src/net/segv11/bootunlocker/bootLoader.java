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
	public static final long delayAfterChange = 200;	// 200ms
	private static final long launchDelay = 30;			// 30ms

	/** For logging */
	private static final String TAG = "net.segv11.bootloader";
	
    /** checks if we know how to lock/unlock the bootloader on this device */
    public static bootLoader makeBootLoader() {
    	Log.v(TAG, "DEVICE = " + android.os.Build.DEVICE);
    	if (android.os.Build.DEVICE.equals("maguro")) {
    		return new bootLoader_Gnex();
    	} else if (android.os.Build.DEVICE.equals("toro")) {
    		return new bootLoader_Gnex();
    	} else if (android.os.Build.DEVICE.equals("toroplus")) {
    		return new bootLoader_Gnex();
    	} else if (android.os.Build.DEVICE.equals("manta")) {
    		return new bootLoader_N10();
    	} else if (android.os.Build.DEVICE.equals("mako")) {
    		return new bootLoader_N4();
    	} else {
    		return null;
    	}
    	// TODO: Should we check android.os.Build.BOOTLOADER ?
    }

    
    /** Locks or unlocks the bootloader */
    public void setLockState(boolean newState) throws IOException {
    	// We override this in subclasses
    	return;
    }
  
    
    /** Finds out (from the param partition) if the bootloader is unlocked */
    public int getLockState() {
    	// We override this in subclasses
		return BL_UNKNOWN;
   }
     
    /** Low-level code for pushing a write command through SU */
	public void superUserCommandWithDataByte(String theCommand, int dataByte) throws IOException {
		Process p = Runtime.getRuntime().exec("su");
	    DataOutputStream w=new DataOutputStream(p.getOutputStream());
	    // BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    w.writeBytes(theCommand + dataByte +"\n");	// dataByte here is just for logging
	    w.flush();
	    
	    // Wait for the command to launch
    	try {
			Thread.sleep(launchDelay);
		} catch (InterruptedException e) {
			// Should not happen; if it does, we just keep going.
			e.printStackTrace();
		}
	    
	    w.writeByte(dataByte);
	    w.flush();
	    w.close();				
	}

    /** Low-level code for pushing a query command through SU */
	public int superUserCommandWithByteResult(String theCommand) throws IOException {
		//Process p = Runtime.getRuntime().exec(new String[]{"su", "-c", queryCommand});
		//int isLocked = p.getInputStream().read();

		
		Process p = Runtime.getRuntime().exec("su");
	    DataOutputStream w =new DataOutputStream(p.getOutputStream());
	    DataInputStream r = new DataInputStream(p.getInputStream());
	    
	    w.writeBytes(theCommand+"\n");
	    w.flush();
	    int resultByte = r.readByte();
	    // w.writeBytes("exit\n");
	    // w.flush();
	    w.close();	
	    return resultByte;
	}  
  
}
