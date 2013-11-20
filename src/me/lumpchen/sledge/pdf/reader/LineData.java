package me.lumpchen.sledge.pdf.reader;


public class LineData {
	
	private byte[] source;
	
	public LineData(byte[] data) {
		this.source = data;
	}
	
	public long readAsLong() {
		int len = source.length;
		int value = 0;
		for (int i = 0, n = source.length; i < n; i++) {
			int c = Character.digit(source[i], 10);
			value += c * (int) (Math.pow(10, len - i - 1) + 0.5);
		}
		return value;
	}
	
	public byte[] getData() {
		return this.source;
	}
	
	public String readAsString() {
		return new String(this.source);
	}
	
	public boolean startsWith(byte[] prefix) {
		if (source.length < prefix.length) {
			return false;
		}
		for (int i = 0, n = prefix.length; i < n; i++) {
			if (source[i] != prefix[i]) {
				return false;
			}
		}
		return true;
	}
	
	public boolean contain(byte[] dst) {
		if (source.length < dst.length) {
			return false;
		}
		byte first = dst[0];
		int i = 0;
		while (true) {
			if (i >= source.length) {
				break;
			}
			if (first == source[i]) {
				int j = 0;
				while (source[++i] == dst[++j]) {
					if (j == dst.length - 1) {
						return true;
					}
				}
			}
			i++;
		}

		return false;
	}

}
