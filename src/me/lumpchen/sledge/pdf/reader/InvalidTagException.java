package me.lumpchen.sledge.pdf.reader;

public class InvalidTagException extends ReadException {

	private static final long serialVersionUID = -1859314583029669826L;

	public InvalidTagException() {
		super();
	}

	public InvalidTagException(String message) {
		super(message);
	}

}
