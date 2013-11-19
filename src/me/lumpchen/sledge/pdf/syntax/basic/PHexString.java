package me.lumpchen.sledge.pdf.syntax.basic;



public class PHexString extends PString {

	public static final byte BEGIN = '<';
	public static final byte END = '>';
	
	public PHexString() {
		super();
	}
	
	public PHexString(byte[] data) {
		encode(data);
	}

	protected void encode(byte[] data) {
	}
}
