package com.docscience.pathfinder.font.shared;


/* 
 * because of GWT Issue 8239: https://code.google.com/p/google-web-toolkit/issues/detail?id=8239
 * we have to use the loop to copy array.
 */
public final class ArrayUtil {

	private final static int LIMIT = 4096;
	
	public static boolean[] copyOf(boolean[] src, int len) {
		final int N = LIMIT;
		boolean[] tar = new boolean[len];
		int limit = Math.min(src.length, len);
		for (int i = 0, j = (limit + N - 1) / N; i < j; i++) {			
			System.arraycopy(src, i * N, tar, i * N, Math.min(N, limit - (i * N)));
		}
		return tar;
	}
	
	public static byte[] copyOf(byte[] src, int len) {
		final int N = LIMIT;
		byte[] tar = new byte[len];
		int limit = Math.min(src.length, len);
		for (int i = 0, j = (limit + N - 1) / N; i < j; i++) {			
			System.arraycopy(src, i * N, tar, i * N, Math.min(N, limit - (i * N)));
		}
		return tar;
	}
	
	public static char[] copyOf(char[] src, int len) {
		final int N = LIMIT / 2;
		char[] tar = new char[len];
		int limit = Math.min(src.length, len);
		for (int i = 0, j = (limit + N - 1) / N; i < j; i++) {			
			System.arraycopy(src, i * N, tar, i * N, Math.min(N, limit - (i * N)));
		}
		return tar;
	}

	public static short[] copyOf(short[] src, int len) {
		final int N = LIMIT / 2;
		short[] tar = new short[len];
		int limit = Math.min(src.length, len);
		for (int i = 0, j = (limit + N - 1) / N; i < j; i++) {			
			System.arraycopy(src, i * N, tar, i * N, Math.min(N, limit - (i * N)));
		}
		return tar;
	}
	
	public static int[] copyOf(int[] src, int len) {
		final int N = LIMIT / 4;
		int[] tar = new int[len];
		int limit = Math.min(src.length, len);
		for (int i = 0, j = (limit + N - 1) / N; i < j; i++) {			
			System.arraycopy(src, i * N, tar, i * N, Math.min(N, limit - (i * N)));
		}
		return tar;
	}

	public static long[] copyOf(long[] src, int len) {
		final int N = LIMIT / 8;
		long[] tar = new long[len];
		int limit = Math.min(src.length, len);
		for (int i = 0, j = (limit + N - 1) / N; i < j; i++) {			
			System.arraycopy(src, i * N, tar, i * N, Math.min(N, limit - (i * N)));
		}
		return tar;
	}
	
	public static float[] copyOf(float[] src, int len) {
		final int N = LIMIT / 4;
		float[] tar = new float[len];
		int limit = Math.min(src.length, len);
		for (int i = 0, j = (limit + N - 1) / N; i < j; i++) {			
			System.arraycopy(src, i * N, tar, i * N, Math.min(N, limit - (i * N)));
		}
		return tar;
	}

	public static double[] copyOf(double[] src, int len) {
		final int N = LIMIT / 8;
		double[] tar = new double[len];
		int limit = Math.min(src.length, len);
		for (int i = 0, j = (limit + N - 1) / N; i < j; i++) {			
			System.arraycopy(src, i * N, tar, i * N, Math.min(N, limit - (i * N)));
		}
		return tar;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] copyOf(T[] src, int len) {
		final int N = LIMIT / 4;
		Object[] tar = new Object[len];
		int limit = Math.min(src.length, len);
		for (int i = 0, j = (limit + N - 1) / N; i < j; i++) {			
			System.arraycopy(src, i * N, tar, i * N, Math.min(N, limit - (i * N)));
		}
		return (T[]) tar;
	}

	private ArrayUtil() {
		// prevent instance
	}
	
}
