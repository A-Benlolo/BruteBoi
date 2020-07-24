package crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DES {
	public static byte[] encrypt(String text, byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		// Get the key and cipher
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

		// Create the key
		SecretKey convertedKey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key));

		// Initialize the cipher
		cipher.init(Cipher.ENCRYPT_MODE, convertedKey);

		// Return the encrypted data
		return cipher.doFinal(text.getBytes());
	}
	
	public static byte[] encryptRandom(String text, int keyLength) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		// Get the key and cipher
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

		// Create random bytes for the key
		SecureRandom rand = new SecureRandom();
		byte[] key = new byte[8];
		rand.nextBytes(key);
		for(int i = keyLength; i < key.length; i++)
			key[i] = 0;
		
		// Create the key
		SecretKey convertedKey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key));

		// Initialize the cipher
		cipher.init(Cipher.ENCRYPT_MODE, convertedKey);

		// Return the encrypted data
		return cipher.doFinal(text.getBytes());
	}

	public static byte[] decrypt(byte[] text, byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		// Get the key and cipher
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

		// Create the key
		SecretKey convertedKey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key));

		// Initialize the cipher
		cipher.init(Cipher.DECRYPT_MODE, convertedKey);

		// Return the encrypted data
		return cipher.doFinal(text);
	}
}