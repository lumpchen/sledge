package me.lumpchen.sledge.pdf.reader;

public class InvalidNameException extends RuntimeException {

	private static final long serialVersionUID = 3573032348155444665L;

	public InvalidNameException(String name) {
		super(name);
	}
}
