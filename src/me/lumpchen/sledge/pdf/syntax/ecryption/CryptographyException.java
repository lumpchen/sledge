package me.lumpchen.sledge.pdf.syntax.ecryption;

public class CryptographyException extends Exception {

	private static final long serialVersionUID = -5631872065508857171L;

	public enum CausedBy {
		wrongPassword
	};

	private boolean wrongPassword = false;

	public CryptographyException() {
		super();
	}

	public CryptographyException(Exception e) {
		super(e);
	}

	public CryptographyException(String msg) {
		super(msg);
	}

	public CryptographyException(String msg, CausedBy cause) {
		super(msg);

		if (cause == CausedBy.wrongPassword) {
			this.wrongPassword = true;
		}
	}

	public boolean causedByWrongPassword() {
		return this.wrongPassword;
	}
}
