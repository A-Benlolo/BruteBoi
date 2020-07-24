package mainPackage;

import crypto.DES;

public class DESBreakingThread extends Thread {
	// Private data
	private byte[] encryptedBytes;
	private byte[] startingKey;
	private byte[] currKey;
	private int attemptCount;
	
	/**
	 * Create a new DESBreakingThread.
	 * 
	 * @param encryptedBytes The encrypted bytes to try to break.
	 * @param startingKey The starting position of the key
	 */
	public DESBreakingThread(byte[] encryptedBytes, byte[] startingKey) {
		this.encryptedBytes = new byte[encryptedBytes.length];
		for(int i = 0; i < encryptedBytes.length; i++)
			this.encryptedBytes[i] = encryptedBytes[i];
		this.startingKey = new byte[startingKey.length];
		for(int i = 0; i < startingKey.length; i++)
			this.startingKey[i] = startingKey[i];
		this.currKey = new byte[startingKey.length];
		for(int i = 0; i < startingKey.length; i++)
			this.currKey[i] = startingKey[i];
		attemptCount = 0;
	}
	
	/**
	 * Start the process of brute forcing.
	 */
	public void run() {
		boolean success = false;
		byte[] decrypted = new byte[1];
		while(!success) {
			try {
				decrypted = DES.decrypt(encryptedBytes, currKey);
				
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
				attemptCount++;
				
				success = false;
			}
		}
		System.out.print("Key: ");
		for(byte b : currKey)
			System.out.print(b + " ");
		System.out.println();
		System.out.println("Message: "+new String(decrypted));
		System.exit(0);
	}

	public byte[] getCurrKey() {
		return currKey;
	}

	public void setCurrKey(byte[] currKey) {
		this.currKey = currKey;
	}

	public int getAttemptCount() {
		return attemptCount;
	}

	public void setAttemptCount(int attemptCount) {
		this.attemptCount = attemptCount;
	}
}
