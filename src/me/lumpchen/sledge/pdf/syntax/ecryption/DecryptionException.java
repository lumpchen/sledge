package me.lumpchen.sledge.pdf.syntax.ecryption;

public class DecryptionException extends Exception {

	private static final long serialVersionUID = -7422967197304476084L;

	public DecryptionException() {
		super();
	}

	public DecryptionException(Exception e) {
		super(e);
	}

	public DecryptionException(String msg) {
		super(msg);
	}

}
