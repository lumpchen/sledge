package com.docscience.pathfinder.font.driver.afp;

/**
 * @author wxin
 *
 */
public final class MODCANumber {
	
	private MODCANumber() {}
	
	public static final int  SBIN1_MAX =  127;
	public static final int  SBIN1_MIN = -128;
	public static final int  SBIN2_MAX =  32767;
	public static final int  SBIN2_MIN = -32768;
	public static final int  SBIN3_MAX =  8388607;
	public static final int  SBIN3_MIN = -8388608;
	public static final int  SBIN4_MAX =  2147483647;
	public static final int  SBIN4_MIN = -2147483648;
	public static final int  UBIN1_MAX =  255;
	public static final int  UBIN1_MIN =  0;
	public static final int  UBIN2_MAX =  65535;
	public static final int  UBIN2_MIN =  0;
	public static final int  UBIN3_MAX =  16777216;
	public static final int  UBIN3_MIN =  0;
	public static final long UBIN4_MAX =  4294967296L;
	public static final long UBIN4_MIN =  0;
	
	public static byte getSBIN1(byte[] data, int offset) {
		return (byte) getSBIN(data, offset, 1);
	}
	
	public static short getSBIN2(byte[] data, int offset) {
		return (short) getSBIN(data, offset, 2);
	}
	
	public static int getSBIN3(byte[] data, int offset) {
		return getSBIN(data, offset, 3);
	}
	
	public static int getSBIN4(byte[] data, int offset) {
		return getSBIN(data, offset, 4);
	}
	
	public static int getSBIN(byte[] data, int offset, int nBytes) {
		assert(nBytes > 0 && nBytes <= 4);
		long ubin = 0;
		for (int i=0; i<nBytes; ++i) {
			ubin = (ubin << 8) | (data[offset + i] & 0xff);
		}
		long max_sbin = (1L << (nBytes * 8)) - 1;
		if (ubin > max_sbin) {
			ubin = max_sbin - ubin;
		}
		return (int) ubin;
	}
	
	public static short getUBIN1(byte[] data, int offset) {
		return (short) getUBIN(data, offset, 1);
	}

	public static int getUBIN2(byte[] data, int offset) {
		return (int) getUBIN(data, offset, 2);
	}

	public static int getUBIN3(byte[] data, int offset) {
		return (int) getUBIN(data, offset, 3);
	}

	public static long getUBIN4(byte[] data, int offset) {
		return (long) getUBIN(data, offset, 4);
	}

	public static long getUBIN(byte[] data, int offset, int nBytes) {
		assert(nBytes > 0 && nBytes <= 4);
		long ubin = 0;
		for (int i=0; i<nBytes; ++i) {
			ubin = (ubin << 8) | (data[offset + i] & 0xff);
		}
		return ubin;		
	}

	public static void putSBIN1(int number, byte[] data, int offset) {
		putSBIN(number, 1, data, offset);
	}
	
	public static void putSBIN2(int number, byte[] data, int offset) {
		putSBIN(number, 2, data, offset);
	}

	public static void putSBIN3(int number, byte[] data, int offset) {
		putSBIN(number, 3, data, offset);
	}
	
	public static void putSBIN4(int number, byte[] data, int offset) {
		putSBIN(number, 4, data, offset);
	}
	
	public static void putSBIN(int number, int nBytes, byte[] data, int offset) {
		assert(nBytes > 0 && nBytes <= 4);
		for (int i=0; i<nBytes; ++i) {
			data[offset + i] = (byte) (number >> (8 * (nBytes - i - 1)));
		}
	}

	public static void putUBIN1(int number, byte[] data, int offset) {
		putUBIN(number, 1, data, offset);
	}
	
	public static void putUBIN2(int number, byte[] data, int offset) {
		putUBIN(number, 2, data, offset);
	}
	
	public static void putUBIN3(int number, byte[] data, int offset) {
		putUBIN(number, 3, data, offset);
	}

	public static void putUBIN4(long number, byte[] data, int offset) {
		putUBIN(number, 4, data, offset);
	}

	public static void putUBIN(long number, int nBytes, byte[] data, int offset) {
		assert(nBytes > 0 && nBytes <= 4);
		for (int i=0; i<nBytes; ++i) {
			data[offset + i] = (byte) (number >> (8 * (nBytes - i - 1)));
		}		
	}
	
}
