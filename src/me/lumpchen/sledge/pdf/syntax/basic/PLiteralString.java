package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.reader.InvalidTagException;
import me.lumpchen.sledge.pdf.reader.ObjectReader;

public class PLiteralString extends PString {

	public static final byte BEGIN = '(';
	public static final byte END = ')';

	public PLiteralString() {
		super();
	}

	public PLiteralString(byte[] bytes) {
		encode(bytes);
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("(");
		if (charSequence != null) {
			buf.append(new String(charSequence));
		}
		buf.append(")");
		return buf.toString();
	}
	
	@Override
	protected void readBeginTag(ObjectReader reader) {
		byte tag = reader.readByte();
		if (tag != BEGIN) {
			throw new InvalidTagException();
		}		
	}

	@Override
	protected void readBody(ObjectReader reader) {
		byte[] data = reader.readToFlag(END);
		this.encode(data);		
	}

	@Override
	protected void readEndTag(ObjectReader reader) {
		byte tag = reader.readByte();
		if (tag != END) {
			throw new InvalidTagException();
		}			
	}

	@Override
	protected void encode(byte[] data) {
		escape(data);
	}

	private void escape(byte[] data) {
		int size = data.length;
		char[] chars = new char[size];

		boolean bsFound = false;
		int skip = 0;
		for (int i = 0; i < size; i++) {
			byte b = data[i];
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
