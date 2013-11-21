package me.lumpchen.sledge.pdf.reader;

public class NotMatchedTagException extends ReadException {

	private static final long serialVersionUID = 1123012115180389067L;
	
	public NotMatchedTagException(String message) {
		super(message);
	}

}
