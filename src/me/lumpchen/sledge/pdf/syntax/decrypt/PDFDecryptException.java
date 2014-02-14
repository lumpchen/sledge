package me.lumpchen.sledge.pdf.syntax.decrypt;

public class PDFDecryptException extends Exception {

	private static final long serialVersionUID = 1L;

	public PDFDecryptException() {
		super();
	}
	
	public PDFDecryptException(String msg) {
		super(msg);
	}
	
	
	public PDFDecryptException(String msg, Throwable e) {
		super(msg, e);
	}
}
