package me.lumpchen.sledge.pdf.syntax.ecryption;

public class BadSecurityHandlerException extends Exception {

	private static final long serialVersionUID = -5298593964994403226L;

	public BadSecurityHandlerException() {
		super();
	}

	public BadSecurityHandlerException(Exception e) {
		super(e);
	}

	public BadSecurityHandlerException(String msg) {
		super(msg);
	}

}
