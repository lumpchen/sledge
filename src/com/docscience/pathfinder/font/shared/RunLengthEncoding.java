package com.docscience.pathfinder.font.shared;


public final class RunLengthEncoding {

	public static byte[] diff(byte[] src) {
		return diff(src, 0, src.length);
	}
	
	public static byte[] diff(byte[] src, int offset, int length) {
		byte[] tar = new byte[length];
		tar[0] = src[offset];

		int last = src[offset];
		for (int i = 1; i < tar.length; i++) {
			tar[i] = (byte) (src[offset + i] - last);
			last = src[offset + i];
		}
		return tar;
	}

	public static byte[] flat(byte[] src) {
		return flat(src, 0, src.length);
	}
	
	public static byte[] flat(byte[] src, int offset, int length) {
		byte[] tar = new byte[length];
		tar[0] = src[offset];

		int last = tar[0];
		for (int i = 1; i < tar.length; i++) {
			tar[i] = (byte) (src[offset + i] + last);
			last = tar[i];
		}
		return tar;
	}

	public static byte[] encode(byte[] src) {
		return encode(src, 0, src.length);
	}
	
	public static byte[] encode(byte[] src, int offset, int length) {
		byte[] tar = new byte[length * 2];
		int v = src[offset];
		int n = 1;
		int t = 0;

		for (int i = 1; i < length; i++) {
			if (src[offset + i] != v || n >= 0xff) {
				tar[t++] = (byte) n;
				tar[t++] = (byte) v;
				n = 1;
				v = src[offset + i];
			} else {
				n++;
			}
		}
		tar[t++] = (byte) n;
		tar[t++] = (byte) v;

		return ArrayUtil.copyOf(tar, t);
	}

	public static byte[] decode(byte[] src) {
		return decode(src, 0, src.length);
	}
	
	public static byte[] decode(byte[] src, int offset, int length) {
		byte[] tar = new byte[length * 2];
		int t = 0;
		for (int i = 0; i < length; i += 2) {
			int n = src[offset + i] & 0xff;
			int v = src[offset + i + 1];
			for (int j = 0; j < n; j++) {
				if (t >= tar.length) {
					tar = ArrayUtil.copyOf(tar, tar.length * 2);
				}
				tar[t++] = (byte) v;
			}
		}

		return ArrayUtil.copyOf(tar, t);
	}

	public static byte[] diffAndEncode(byte[] src) {
		return diffAndEncode(src, 0, src.length);
	}
	
	public static byte[] diffAndEncode(byte[] src, int offset, int length) {
		return encode(diff(src, offset, length));
	}

	public static byte[] decodeAndFlat(byte[] src) {
		return decodeAndFlat(src, 0, src.length);
	}
	
	public static byte[] decodeAndFlat(byte[] src, int offset, int length) {
		return flat(decode(src));
	}

	public static short[] diff(short[] src) {
		return diff(src, 0, src.length);
	}
	
	public static short[] diff(short[] src, int offset, int length) {
		short[] tar = new short[length];
		tar[0] = src[offset];

		int last = src[offset];
		for (int i = 1; i < tar.length; i++) {
			tar[i] = (short) (src[offset + i] - last);
			last = src[offset + i];
		}
		return tar;
	}

	public static short[] flat(short[] src) {
		return flat(src, 0, src.length);
	}
	
	public static short[] flat(short[] src, int offset, int length) {
		short[] tar = new short[length];
		tar[0] = src[offset];

		int last = tar[0];
		for (int i = 1; i < tar.length; i++) {
			tar[i] = (short) (src[offset + i] + last);
			last = tar[i];
		}
		return tar;
	}

	public static short[] encode(short[] src) {
		return encode(src, 0, src.length);
	}
	
	public static short[] encode(short[] src, int offset, int length) {
		short[] tar = new short[length * 2];
		int v = src[offset];
		int n = 1;
		int t = 0;

		for (int i = 1; i < length; i++) {
			if (src[offset + i] != v || n >= 0xffff) {
				tar[t++] = (short) n;
				tar[t++] = (short) v;
				n = 1;
				v = src[offset + i];
			} else {
				n++;
			}
		}
		tar[t++] = (short) n;
		tar[t++] = (short) v;

		return ArrayUtil.copyOf(tar, t);
	}

	public static short[] decode(short[] src) {
		return decode(src, 0, src.length);
	}
	
	public static short[] decode(short[] src, int offset, int length) {
		short[] tar = new short[length * 2];
		int t = 0;
		for (int i = 0; i < length; i += 2) {
			int n = src[offset + i] & 0xffff;
			int v = src[offset + i + 1];
			for (int j = 0; j < n; j++) {
				if (t >= tar.length) {
					tar = ArrayUtil.copyOf(tar, tar.length * 2);
				}
				tar[t++] = (short) v;
			}
		}

		return ArrayUtil.copyOf(tar, t);
	}

	public static short[] diffAndEncode(short[] src) {
		return diffAndEncode(src, 0, src.length);
	}
	
	public static short[] diffAndEncode(short[] src, int offset, int length) {
		return encode(diff(src, offset, length));
	}

	public static short[] decodeAndFlat(short[] src) {
		return decodeAndFlat(src, 0, src.length);
	}
	
	public static short[] decodeAndFlat(short[] src, int offset, int length) {
		return flat(decode(src, offset, length));
	}

	private RunLengthEncoding() {
		// do nothing here;
	}

}
