package mainPackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.Timer;

import crypto.DES;

public class SingleThread {
	
	// The timer
	public static Timer timer = new Timer(1000, updateTime());
	public static int howManyAttempts = 0;
	public static LinkedList<Integer> counts = new LinkedList<Integer>();
	
	public static void main(String[] args) {
		boolean success = false;
		byte[] encrypted = null, decrypted = null;

		// Encrypt the data
		try {
			encrypted = DES.encryptRandom("Go eat some lunch, you handsome bastard!", 3);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Attempt to decrypt
		timer.start();
		byte[] currKey = new byte[8];
		while(!success) {
			try {
				decrypted = DES.decrypt(encrypted, currKey);
				
				if(new String(decrypted).matches("^[\\u0000-\\u007F]*$"))
					success = true;
				else
					throw new Exception();
			} catch (Exception e) {
				// Increment the key
				for(int i = 0; i < currKey.length; i++) {
					if(currKey[i] == -1) {
						currKey[i] = 0;
					}
					else {
						currKey[i] = (byte)(currKey[i]+1);
						break;
					}
				}
				
				// Increment the number of attempts
				howManyAttempts++;
				
				success = false;
			}
		}
		// Stop the timer and tell the user
		timer.stop();
		System.out.print("Key Found: ");
		for(byte b : currKey)
			System.out.print(b+" ");
		System.out.println();
		
		// Determine average time
		double avg = 0;
		for(Integer i : counts)
			avg += i;
		avg = avg / counts.size();
		
		// Print the findings
		System.out.println("Average attempts per minute: "+avg);
		System.out.println("Message: "+new String(decrypted));
	}
	
	// Update the value of a clock
	private static ActionListener updateTime() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println((counts.size()+1)+" minutes since start. "+howManyAttempts+" attempts in the past minute.");
				counts.add(howManyAttempts);
				howManyAttempts = 0;
			}
		};
	}
}