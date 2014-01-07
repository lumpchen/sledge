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

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("<");
		if (charSequence != null) {
			buf.append(new String(charSequence));
		}
		buf.append(">");
		return buf.toString();
	}
	
	protected void encode(byte[] data) {
		this.charSequence = new char[data.length];
		for (int i = 0; i < data.length; i++) {
			this.charSequence[i] = (char) data[i];
		}
	}

}
