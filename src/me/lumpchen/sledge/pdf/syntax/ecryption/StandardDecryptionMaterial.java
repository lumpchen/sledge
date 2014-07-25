package me.lumpchen.sledge.pdf.syntax.ecryption;

import java.io.UnsupportedEncodingException;

public class StandardDecryptionMaterial implements DecryptionMaterial {

	private byte[] password = null;

	public StandardDecryptionMaterial(String pwd) {
		try {
			this.password = pwd.getBytes("ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public StandardDecryptionMaterial(byte[] pwd) {
		password = pwd;
	}

	public byte[] getPassword() {
		return password;
	}
}
