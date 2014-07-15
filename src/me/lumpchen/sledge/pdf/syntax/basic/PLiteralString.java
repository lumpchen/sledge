package me.lumpchen.sledge.pdf.syntax.basic;


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
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7': {
				if (bsFound) {
					char b0 = '0';
					char b1 = '0';
					char b2 = (char) b;
					char next;
					if (i + 1 < size) {
						next = (char) data[i + 1];
						if (next >= '0' && next <= '7') {
							b1 = next;
							skip++;
							i++;
							
							if (i + 1 < size) {
								next = (char) data[i + 1];
								if (next >= '0' && next <= '7') {
									b0 = next;
									skip++;
									i++;
								} 
							}
						}
					}
					
					String s = "" + b2 + b1 + b0;
					int dec = Integer.parseInt(s, 8);
					chars[i - skip] = (char) dec;
					bsFound = false;
				} else {
					chars[i - skip] = (char) b;	
				}
				break;
			}
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
