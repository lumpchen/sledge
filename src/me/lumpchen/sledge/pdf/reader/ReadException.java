package me.lumpchen.sledge.pdf.reader;

public class ReadException extends RuntimeException {

	private static final long serialVersionUID = 8153461953017236223L;
	
	public ReadException() {
		super();
	}
	
	public ReadException(String message) {
		super(message);
	}

	public ReadException(Throwable message) {
		super(message);
	}
}
