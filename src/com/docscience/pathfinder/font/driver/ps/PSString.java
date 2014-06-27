package com.docscience.pathfinder.font.driver.ps;


/**
 * @author wxin
 *
 */
public class PSString extends PSCompositeObject {

	public static final int FORMAT_LITERAL = 0;
	public static final int FORMAT_HEX     = 1;
	public static final int FORMAT_ASCII85 = 2;
	
	private static final String hexChars = "0123456789abcdef";

	private boolean executable;
	private byte[]  data;
	private int     format;

	public PSString(String asciiString) {
		this.format = FORMAT_LITERAL;
		this.data = new byte[asciiString.length()];
		for (int i=0; i<data.length; ++i) {
			this.data[i] = (byte) (asciiString.charAt(i) & 0xff);
		}
	}
	
	public PSString(byte[] data) {
		this(data, 0, data.length);
	}
	
	public PSString(byte[] data, int offset, int length) {
		this.format = FORMAT_HEX;
		this.data = new byte[length];
		System.arraycopy(data, offset, this.data, 0, length);
	}

	public byte[] getBytes() {
		return data;
	}
	
	@Override
	public int getType() {
		return TYPE_STRING;
	}
	
	public int getFormat() {
		return format;
	}
	
	public void setFormat(int format) {
		this.format = format;
	}

	@Override
	public boolean isExecutable() {
		return this.executable;
	}

	@Override
	public void setExecutable(boolean value) {
		this.executable = value;
	}

	@Override
	public String toString() {
		if (format == FORMAT_LITERAL) {
			return formatLiteralString(data);
		}
		else if (format == FORMAT_HEX) {
			return formatHexString(data);
		}
		else {
			assert(format == FORMAT_ASCII85);
			return formatASCII85String(data);
		}
	}
	
	public String getASCIIString() {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<data.length; ++i) {
			sb.append((char) (data[i] & 0xff));
		}
		return sb.toString();
	}

	public static String formatASCII85String(byte[] data2) {
		StringBuffer sb = new StringBuffer();
		sb.append("<~");
		assert(false) : "ascii85 is not implemented yet";
		sb.append("~>");
		return sb.toString();
	}

	public static String formatHexString(byte[] data) {
		StringBuffer sb = new StringBuffer();
		sb.append('<');
		for (int i=0; i<data.length; ++i) {
			int c = data[i];
			sb.append((char) hexChars.charAt((c >> 4) & 0x0f));
			sb.append((char) hexChars.charAt(c & 0x0f));
		}
		sb.append('>');
		return sb.toString();
	}

	public static String formatLiteralString(byte[] data) {
		StringBuffer sb = new StringBuffer();
		sb.append('(');
		for (int i=0; i<data.length; ++i) {
			int c = data[i];
			if (c >= 32 && c < 128) {
				switch(c) {
				case '\n': sb.append("\\n"); break;
				case '\r': sb.append("\\r"); break;
				case '\t': sb.append("\\t"); break;
				case '\b': sb.append("\\b"); break;
				case '\f': sb.append("\\f"); break;
				case '\\': sb.append("\\\\"); break;
				case '(' : sb.append("\\("); break;
				case ')' : sb.append("\\)"); break;
				default:
					sb.append((char) c);
				}
			}
			else {
				sb.append('\\');
				sb.append('0' + ((c >> 6) & 7));
				sb.append('0' + ((c >> 3) & 7));
				sb.append('0' + (c & 7));
			}
		}
		sb.append(')');
		return sb.toString();
	}

	public int length() {
		return data.length;
	}
	
}
