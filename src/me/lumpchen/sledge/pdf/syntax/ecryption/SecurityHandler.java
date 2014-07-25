package me.lumpchen.sledge.pdf.syntax.ecryption;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import me.lumpchen.sledge.pdf.syntax.lang.PArray;

public abstract class SecurityHandler {

	private static final int DEFAULT_KEY_LENGTH = 40;

	protected int version;

	protected int keyLength = DEFAULT_KEY_LENGTH;

	private boolean aes;

	protected byte[] encryptionKey;
	protected AccessPermission currentAccessPermission = null;
	protected ARCFour rc4 = new ARCFour();

	private static final byte[] AES_SALT = { (byte) 0x73, (byte) 0x41, (byte) 0x6c, (byte) 0x54 };

	public abstract void prepareForDecryption(PDEncryptionDictionary encDictionary,
			PArray documentIDArray, DecryptionMaterial decryptionMaterial)
			throws CryptographyException, IOException;

	public boolean isAES() {
		return aes;
	}

	public void setAES(boolean aesValue) {
		aes = aesValue;
	}

	public int getKeyLength() {
		return keyLength;
	}

	public void setKeyLength(int keyLen) {
		this.keyLength = keyLen;
	}

	public AccessPermission getCurrentAccessPermission() {
		return currentAccessPermission;
	}

	public void encryptData(long objectNumber, long genNumber, InputStream data, OutputStream output)
			throws CryptographyException, IOException {
		if (aes) {
			throw new IllegalArgumentException("AES encryption is not yet implemented.");
		}
		this.encodeData(objectNumber, genNumber, data, output, false);
	}

	public byte[] decryptData(long objectNumber, long genNumber, byte[] data)
			throws CryptographyException, IOException {
		InputStream in = new ByteArrayInputStream(data);
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		this.decryptData(objectNumber, genNumber, in, out);
		return out.toByteArray();
	}

	public void decryptData(long objectNumber, long genNumber, InputStream data, OutputStream output)
			throws CryptographyException, IOException {
		this.encodeData(objectNumber, genNumber, data, output, true);
	}

	private void encodeData(long objectNumber, long genNumber, InputStream data,
			OutputStream output, boolean decrypt) throws CryptographyException, IOException {
		byte[] newKey = new byte[encryptionKey.length + 5];
		System.arraycopy(encryptionKey, 0, newKey, 0, encryptionKey.length);

		// PDF 1.4 reference pg 73
		// step 1
		// we have the reference

		// step 2
		newKey[newKey.length - 5] = (byte) (objectNumber & 0xff);
		newKey[newKey.length - 4] = (byte) ((objectNumber >> 8) & 0xff);
		newKey[newKey.length - 3] = (byte) ((objectNumber >> 16) & 0xff);
		newKey[newKey.length - 2] = (byte) (genNumber & 0xff);
		newKey[newKey.length - 1] = (byte) ((genNumber >> 8) & 0xff);

		// step 3
		byte[] digestedKey = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(newKey);
			if (aes) {
				md.update(AES_SALT);
			}
			digestedKey = md.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new CryptographyException(e);
		}

		// step 4
		int length = Math.min(newKey.length, 16);
		byte[] finalKey = new byte[length];
		System.arraycopy(digestedKey, 0, finalKey, 0, length);

		if (aes) {
			byte[] iv = new byte[16];

			data.read(iv);

			try {
				Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				SecretKey aesKey = new SecretKeySpec(finalKey, "AES");
				IvParameterSpec ips = new IvParameterSpec(iv);
				decryptCipher
						.init(decrypt ? Cipher.DECRYPT_MODE : Cipher.ENCRYPT_MODE, aesKey, ips);

				CipherInputStream cipherStream = new CipherInputStream(data, decryptCipher);

				try {
					byte[] buffer = new byte[4096];
					for (int n = 0; -1 != (n = cipherStream.read(buffer));) {
						output.write(buffer, 0, n);
					}
				} finally {
					cipherStream.close();
				}
			} catch (InvalidKeyException | InvalidAlgorithmParameterException
					| NoSuchPaddingException | NoSuchAlgorithmException e) {
				throw new IOException(e);
			}
		} else {
			rc4.setKey(finalKey);
			rc4.write(data, output);
		}

		output.flush();
	}
}
