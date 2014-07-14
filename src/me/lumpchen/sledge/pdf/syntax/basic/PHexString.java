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
			for (int i = 0; i < this.charSequence.length; i++) {
				String s = Integer.toHexString(this.charSequence[i]);
				if (s.length() == 1) {
					s = "0" + s;
				}
				buf.append(s.toUpperCase());
			}
		}
		buf.append(">");
		return buf.toString();
	}
	
	protected void encode(byte[] data) {
		int n = data.length;
		if (n % 2 > 0) {
			n++;
		}
		char[] cs = new char[n];
		for (int i = 0; i < data.length; i++) {
			cs[i] = 0;
			cs[i] = (char) data[i]; 
		}
		
		this.charSequence = new char[n / 2];
		int i = 0;
		while (i < n) {
			String s = "" + cs[i];
			i++;
			s += cs[i];
			int hex = Integer.parseInt(s, 16);
			this.charSequence[i / 2] = (char) (hex & 0xFF);
			i++;
		}
	}
}
