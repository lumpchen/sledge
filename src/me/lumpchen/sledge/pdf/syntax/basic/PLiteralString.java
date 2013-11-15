package me.lumpchen.sledge.pdf.syntax.basic;


public class PLiteralString extends PString {

	public static final char BEGIN_TAG = '(';
	public static final char END_TAG = ')';
	
	public PLiteralString(byte[] bytes) {
		escape(bytes);
	}

	public String toString() {
		return new String(this.charSequence);
	}

	private void escape(byte[] bytes) {
		int size = bytes.length;
		char[] chars = new char[size];

		boolean bsFound = false;
		int skip = 0;
		for (int i = 0; i < size; i++) {
			byte b = bytes[i];
			switch (b) {
			case BACKSLASH: {
				if (bsFound) {
					chars[i - skip] = BACKSLASH;
					bsFound = false;
				} else {
					bsFound = true;
					skip++;
				}
				break;
			}
			case 'n': {
				if (bsFound) {
					chars[i - skip] = LF;
					bsFound = false;
				} else {
					chars[i - skip] = (char) b;
				}
				break;
			}
			case 'r': {
				if (bsFound) {
					chars[i - skip] = CR;
					bsFound = false;
				} else {
					chars[i - skip] = (char) b;
				}
				break;
			}
			case 't': {
				if (bsFound) {
					chars[i - skip] = TAB;
					bsFound = false;
				} else {
					chars[i - skip] = (char) b;
				}
				break;
			}
			case 'b': {
				if (bsFound) {
					chars[i - skip] = BS;
					bsFound = false;
				} else {
					chars[i - skip] = (char) b;
				}
				break;
			}
			case 'f': {
				if (bsFound) {
					chars[i - skip] = FF;
					bsFound = false;
				} else {
					chars[i - skip] = (char) b;
				}
				break;
			}
			case '(': {
				if (bsFound) {
					chars[i - skip] = LEFT_PARENTHESIS;
					bsFound = false;
				} else {
					chars[i - skip] = (char) b;
				}
				break;
			}
			case ')': {
				if (bsFound) {
					chars[i - skip] = RIGHT_PARENTHESIS;
					bsFound = false;
				} else {
					chars[i - skip] = (char) b;
				}
				break;
			}
			case LF:
			case CR: {
				if (bsFound) {
					skip++;
					bsFound = false;
				} else {
					chars[i - skip] = (char) b;
				}
				break;
			}
			// case '\ddd':{}
			default:
				chars[i - skip] = (char) b;
				break;
			}
		}

		if (chars.length > 0) {
			this.charSequence = new char[size - skip];
			System.arraycopy(chars, 0, this.charSequence, 0, size - skip);
		}
	}
}
