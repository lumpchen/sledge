package me.lumpchen.sledge.pdf.reader;

public class InvalidElementException extends ReadException {

	private static final long serialVersionUID = 6616183350857198444L;

	public InvalidElementException() {
		super();
	}
	
	public InvalidElementException(String message) {
		super(message);
	}

}
