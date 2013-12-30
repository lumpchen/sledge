package me.lumpchen.sledge.pdf.writer;

public class WriteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WriteException() {
		super();
	}
	
	public WriteException(String message) {
		super(message);
	}
}
