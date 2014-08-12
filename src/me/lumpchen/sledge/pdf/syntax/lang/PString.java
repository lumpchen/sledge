package me.lumpchen.sledge.pdf.syntax.lang;

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

	protected char[] charSequence;
	private byte[] bytes;

	protected PString() {
		super.classType = ClassType.String;
	}

	abstract public void encode(byte[] bytes);

	public byte[] getBytes() {
		if (this.bytes != null) {
			return this.bytes;
		}
		this.bytes = new byte[this.charSequence.length];
		for (int i = 0; i < this.charSequence.length; i++) {
			this.bytes[i] = (byte) this.charSequence[i];
		}
		return this.bytes;
	}

	public String toJavaString() {
		if (charSequence != null) {
			return new String(charSequence);
		}

		return "";
	}

	public String toString() {
		if (charSequence != null) {
			return new String(charSequence);
		}

		return "";
	}
}