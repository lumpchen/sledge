package me.lumpchen.sledge.pdf.syntax.ecryption;

public abstract class ProtectionPolicy {

	private static final int DEFAULT_KEY_LENGTH = 40;

	private int encryptionKeyLength = DEFAULT_KEY_LENGTH;

	public void setEncryptionKeyLength(int len) {
		if (len != 40 && len != 128) {
			throw new RuntimeException("Invalid key length '" + len + "' value must be 40 or 128!");
		}
		encryptionKeyLength = len;
	}

	public int getEncryptionKeyLength() {
		return encryptionKeyLength;
	}
}
