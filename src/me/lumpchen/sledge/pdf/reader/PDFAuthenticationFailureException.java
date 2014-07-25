package me.lumpchen.sledge.pdf.reader;

public class PDFAuthenticationFailureException extends Exception {

	private static final long serialVersionUID = -7347387967526998230L;

	public PDFAuthenticationFailureException() {
		super();
	}

	public PDFAuthenticationFailureException(Exception e) {
		super(e);
	}

	public PDFAuthenticationFailureException(String msg) {
		super(msg);
	}
}