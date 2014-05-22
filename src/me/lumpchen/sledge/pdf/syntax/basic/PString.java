package me.lumpchen.sledge.pdf.syntax.basic;

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

	protected PString() {
		super.type = TYPE.String;
	}

	abstract protected void encode(byte[] bytes);

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
	
    /**
     * Get the corresponding byte array for a basic string. This is effectively
     * the char[] array cast to bytes[], as chars in basic strings only use the
     * least significant byte.
     *
     * @param basicString the basic PDF string, as offered by {@link
     *  PDFObject#getStringValue()}
     * @return the bytes corresponding to its characters
     */
    public byte[] asBytes() {
        final byte[] b = new byte[this.charSequence.length];
        for (int i = 0; i < b.length; ++i) {
            b[i] = (byte) this.charSequence[i];
        }
        return b;
    }
}
