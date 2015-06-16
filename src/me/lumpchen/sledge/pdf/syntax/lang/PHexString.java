package me.lumpchen.sledge.pdf.syntax.lang;

public class PHexString extends PString {

	public static final byte BEGIN = '<';
	public static final byte END = '>';

	public PHexString() {
		super();
	}

	public PHexString(byte[] bytes) {
		super();
		this.encode(bytes);
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("<");
		if (this.bytes != null) {
			for (int i = 0; i < this.bytes.length; i++) {
				String s = Integer.toHexString(this.bytes[i] & 0xFF);
				if (s.length() == 1) {
					s = "0" + s;
				}
				buf.append(s.toUpperCase());
			}
		}
		buf.append(">");
		return buf.toString();
	}

	@Override
	public void encode(byte[] bytes) {
		int n = bytes.length;
		if (n % 2 > 0) {
			n++;
		}
		char[] cs = new char[n];
		for (int i = 0; i < bytes.length; i++) {
			cs[i] = 0;
			cs[i] = (char) bytes[i];
		}

		this.bytes = new byte[n / 2];
		int i = 0;
		while (i < n) {
			String s = "" + cs[i];
			i++;
			s += cs[i];
			int hex = Integer.parseInt(s, 16);
			this.bytes[i / 2] = (byte) (hex & 0xFF);
			i++;
		}
	}
}
