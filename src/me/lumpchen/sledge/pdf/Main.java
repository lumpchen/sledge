package me.lumpchen.sledge.pdf;

import java.nio.ByteBuffer;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		byte[] bytes = {1, 1};
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		System.out.println(bb.getShort());
	}
}

