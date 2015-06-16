package me.lumpchen.sledge.pdf.syntax.lang;

import java.io.UnsupportedEncodingException;

public abstract class PString extends PObject {

	public static final byte LF = '\n';
	public static final byte CR = '\r';
	public static final byte BACKSLASH = '\\'; // Backslash
	public static final byte TAB = '\t';
	public static final byte BS = '\b'; // Backspace
	public static final byte FF = '\f'; // Form feed
	public static final byte LEFT_PARENTHESIS = '('; // Left parenthesis
	public static final byte RIGHT_PARENTHESIS = ')'; // Right parenthesis
	public static final byte SPACE = ' ';

	protected byte[] bytes;

	public enum StringType {
		string, text_string, PDFDocEncoded_string, ASCII_string, byte_string
	};

	protected PString() {
		super.classType = ClassType.String;
	}

	abstract public void encode(byte[] bytes);

	public byte[] getBytes() {
		return this.bytes;
	}

	public String toString() {
		if (this.bytes != null) {
			try {
				return new String(this.bytes, "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		return "";
	}
	
	public String encodeString(StringType type) {
		String charsetName = "ISO-8859-1";
		switch (type) {
		case string:
		case PDFDocEncoded_string:
		case ASCII_string:
		case byte_string:
			charsetName = "ISO-8859-1";
			break;
		case text_string:
			charsetName = "UTF-16BE";
			break;
		default:
			charsetName = "ISO-8859-1";
			break;
		}
		
		try {
			return new String(this.bytes, charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
}
