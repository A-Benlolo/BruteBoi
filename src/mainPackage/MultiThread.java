package mainPackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import crypto.DES;

public class MultiThread {
	// The number of bytes in the key
	public static final int KEY_SIZE = 4;
	public static final int DIVISIONS = 10;
	
	// The trackers for speed
	public static Timer timer = new Timer(60000, displayUpdate());
	public static int howManyMinutes = 0;
	public static DESBreakingThread[] threads = null;
	
	public static void main(String[] args) {
		// Encrypt the data
		byte[] encrypted = null;
		try {
			encrypted = DES.encryptRandom("See you in Vegas!", KEY_SIZE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Choose the starting points of the keys
		byte[][] startPos = new byte[DIVISIONS][8];
		for(int i = 0; i < startPos.length; i++)
			startPos[i][KEY_SIZE-1] = (byte)(i*(256/(DIVISIONS)));
		
		// Create the threads
		threads = new DESBreakingThread[DIVISIONS];
		for(int i = 0; i < threads.length; i++)
			threads[i] = new DESBreakingThread(encrypted, startPos[i]);
		
		// Start the threads
		for(DESBreakingThread thread : threads)
			thread.start();
		
		timer.start();
	}
	
	// Display the update in the last chunk of time
	private static ActionListener displayUpdate() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get the number of attempts since last time
				int sum = 0;
				for(DESBreakingThread thread : threads) {
					sum += thread.getAttemptCount();
					thread.setAttemptCount(0);
				}
				System.out.println("Minute "+(++howManyMinutes)+": "+sum+" attempts.");
				
				// Get the current key of each thread
				for(int i = 0; i < threads.length; i++) {
					for(byte b : threads[i].getCurrKey())
						System.out.printf("%4s ", b);
					System.out.println();
				}
				System.out.println();
			}
		};
	}
}
