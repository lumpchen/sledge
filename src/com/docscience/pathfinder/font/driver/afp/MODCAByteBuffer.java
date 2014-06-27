package com.docscience.pathfinder.font.driver.afp;

/**
 * @author wxin
 *
 */
public final class MODCAByteBuffer {
		
	private static final int EXPANDING_SIZE = 32;
	
	private MODCAEncoding encoding = MODCAEncoding.EBCDIC_CS103_CP500;
	private byte[] bytes;
	private int size;
	
	public MODCAByteBuffer() {
		bytes = new byte[EXPANDING_SIZE];
	}
	
	public MODCAByteBuffer(int capacity) {
		bytes = new byte[capacity];
	}
	
	public MODCAByteBuffer(byte[] buffer) {
		bytes = new byte[buffer.length];
		System.arraycopy(buffer, 0, bytes, 0, buffer.length);
		size = buffer.length;
	}
	
	public MODCAEncoding getEncoding() {
		return encoding;
	}
	
	public void setEncoding(MODCAEncoding encoding) {
		this.encoding = encoding;
	}
		
	public byte getByte(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		return bytes[index];
	}
	
	public byte[] getBytes() {
		return getBytes(0, size);
	}
	
	public void setBytes(byte[] buffer) {
		setBytes(buffer, 0, buffer.length);
	}
	
	public void setBytes(byte[] buffer, int offset, int length) {
		size = 0;
		putBytes(buffer, offset, length);
	}
	
	public byte[] getBytes(int index) {
		return getBytes(index, size - index);
	}
	
	public byte[] getBytes(int index, int length) {
		byte[] temp = new byte[length];
		System.arraycopy(bytes, index, temp, 0, length);
		return temp;
	}
		
	public byte[] getBytes(int index, byte[] buffer) {
		return getBytes(index, buffer, 0, buffer.length);
	}
	
	public byte[] getBytes(int index, byte[] buffer, int offset, int length) {
		if (index + length > size) {
			throw new IndexOutOfBoundsException();
		}
		System.arraycopy(bytes, index, buffer, offset, length);
		return buffer;
	}
		
	public void putByte(byte b) {
		expand(1);
		putByte(size - 1, b);
	}
	
	public void putByte(int index, byte b) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		bytes[index] = b;
	}
	
	public void putBytes(byte[] buffer) {
		putBytes(buffer, 0, buffer.length);
	}
	
	public void putBytes(byte[] buffer, int offset, int length) {
		expand(length);
		putBytes(size - length, buffer, offset, length);
	}
	
	public void putBytes(int index, byte[] buffer) {
		putBytes(index, buffer, 0, buffer.length);
	}
	
	public void putBytes(int index, byte[] buffer, int offset, int length) {
		if (index < 0 || index + length > size) {
			throw new IndexOutOfBoundsException();
		}
		System.arraycopy(buffer, offset, bytes, index, length);
	}
	
	public int getSBIN(int index, int nBytes) {
		if (index < 0 || index + nBytes > size) {
			throw new IndexOutOfBoundsException();
		}
		return MODCANumber.getSBIN(bytes, index, nBytes);
	}
		
	public byte getSBIN1(int index) {
		return (byte) getSBIN(index, 1);
	}
	
	public short getSBIN2(int index) {
		return (short) getSBIN(index, 2);
	}
	
	public int getSBIN3(int index) {
		return (int) getSBIN(index, 3);
	}
	
	public int getSBIN4(int index) {
		return getSBIN(index, 4);
	}
	
	public long getUBIN(int index, int nBytes) {
		if (index < 0 || index + nBytes > size) {
			throw new IndexOutOfBoundsException();
		}
		return MODCANumber.getUBIN(bytes, index, nBytes);
	}
	
	public short getUBIN1(int index) {
		return (short) getUBIN(index, 1);
	}
	
	public int getUBIN2(int index) {
		return (int) getUBIN(index, 2);
	}
	
	public int getUBIN3(int index) {
		return (int) getUBIN(index, 3);
	}
	
	public long getUBIN4(int index) {
		return getUBIN(index, 4);
	}
	
	public void putSBIN(int index, int number, int nBytes) {
		if (index < 0 || index + nBytes > size) {
			throw new IndexOutOfBoundsException();
		}
		MODCANumber.putSBIN(number, nBytes, bytes, index);
	}
	
	public void putSBIN1(int index, int number) {
		putSBIN(index, number, 1);
	}
	
	public void putSBIN2(int index, int number) {
		putSBIN(index, number, 2);
	}
	
	public void putSBIN3(int index, int number) {
		putSBIN(index, number, 3);
	}
	
	public void putSBIN4(int index, int number) {
		putSBIN(index, number, 4);
	}
	
	public void putSBIN(int number, int nBytes) {
		expand(nBytes);
		putSBIN(size - nBytes, number, nBytes);
	}
	
	public void putSBIN1(int number) {
		putSBIN(number, 1);
	}
	
	public void putSBIN2(int number) {
		putSBIN(number, 2);
	}
	
	public void putSBIN3(int number) {
		putSBIN(number, 3);
	}
	
	public void putSBIN4(int number) {
		putSBIN(number, 4);
	}
	
	public void putUBIN(int index, long number, int nBytes) {
		if (index < 0 || index + nBytes > size) {
			throw new IndexOutOfBoundsException();
		}
		MODCANumber.putUBIN(number, nBytes, bytes, index);
	}
	
	public void putUBIN1(int index, int number) {
		putUBIN(index, number, 1);
	}
	
	public void putUBIN2(int index, int number) {
		putUBIN(index, number, 2);
	}
	
	public void putUBIN3(int index, int number) {
		putUBIN(index, number, 3);
	}
	
	public void putUBIN4(int index, long number) {
		putUBIN(index, number, 4);
	}
	
	public void putUBIN(long number, int nBytes) {
		expand(nBytes);
		putUBIN(size - nBytes, number, nBytes);
	}
	
	public void putUBIN1(int number) {
		putUBIN(number, 1);
	}
	
	public void putUBIN2(int number) {
		putUBIN(number, 2);
	}
	
	public void putUBIN3(int number) {
		putUBIN(number, 3);
	}
	
	public void putUBIN4(long number) {
		putUBIN(number, 4);
	}
	
	public void setCapacity(int newCapacity) {
		if (newCapacity < size) {
			throw new IllegalArgumentException("capacity smaller than size");
		}
		int capacity = ((newCapacity + EXPANDING_SIZE - 1) / EXPANDING_SIZE) * EXPANDING_SIZE;
		assert(capacity % EXPANDING_SIZE == 0);
		if (capacity > bytes.length) {
			byte[] temp = new byte[capacity];
			System.arraycopy(bytes, 0, temp, 0, size);
			bytes = temp;
		}
		size = newCapacity;
	}
	
	public int getCapacity() {
		return bytes.length;
	}
	
	public int size() {
		return size;
	}
	
	public void expand(int n) {
		setCapacity(size + n);
	}
	
	public char getChar(int index) {
		return encoding.decode(getByte(index));
	}
	
	public void putChar(int index, char c) {
		putByte(index, encoding.encode(c));
	}
	
	public void putChar(char c) {
		putByte(encoding.encode(c));
	}
	
	public String getString(int index, int length) {
		return encoding.decode(getBytes(index, length), 0, length);
	}
		
	public String getASCIIString(int index, int length) {
		char[] chars = new char[length];
		for (int i=0; i<length; ++i) {
			chars[i] = (char) (getByte(i + index) & 0xff);
		}
		return new String(chars);
	}
	
	public void putString(int index, String string, int length) {
		byte[] temp = encoding.encode(string, length);
		for (int i=0; i<length; ++i) {
			if (i < temp.length) {
				putByte(i + index, temp[i]);
			}
			else {
				putByte(i + index, (byte) encoding.getDefaultCharacterCodePoint());
			}
		}
	}
	
	public void putString(int index, String string) {
		putString(index, string, string.length());
	}
	
	public void putString(String string, int length) {
		expand(length);
		putString(size - length, string, length);
	}
	
	public void putString(String string) {
		putString(string, string.length());
	}
	
	public void putASCIIString(int index, String string, int length) {
		for (int i=0; i<length; ++i) {
			if (i < string.length()) {
				putByte(i + index, (byte) string.charAt(i));
			}
			else {
				putByte(i + index, (byte) ' ');
			}
		}
	}
	
	public void putASCIIString(int index, String string) {
		putASCIIString(index, string, string.length());
	}
	
	public void putASCIIString(String string, int length) {
		expand(length);
		putASCIIString(size - length, string, length);
	}
	
	public void putASCIIString(String string) {
		putASCIIString(string, string.length());
	}

	private static final String HEXDIGITS = "0123456789ABCDEF";

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(size * 2 + 2);
		sb.append('<');
		for (int i=0; i<size; ++i) {
			int b = bytes[i] & 0xff;
			sb.append(HEXDIGITS.charAt((b >> 4) & 0xf));
			sb.append(HEXDIGITS.charAt(b & 0xf));
		}
		sb.append('>');
		return sb.toString();
	}
}
