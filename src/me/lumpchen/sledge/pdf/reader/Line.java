package me.lumpchen.sledge.pdf.reader;

import java.nio.ByteBuffer;

import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PInteger;
import me.lumpchen.sledge.pdf.syntax.basic.PLong;
import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class Line {
	
	private byte[] source;
	private ByteBuffer byteBuffer;
	
	public Line(byte[] data) {
		this.source = data;
		this.byteBuffer = ByteBuffer.wrap(source);
	}

	public PName readName() {
		return null;
	}
	
	public PDictionary redDict() {
		return null;
	}
	
	public PInteger readInt() {
		return null;
	}
	
	public PLong readLong() {
		return null;
	}
	
	
	public boolean startsWith(byte[] prefix) {
		if (source.length < prefix.length) {
			return false;
		}
		int i = 0;
		int j = 0;
		while (i < source.length) {
			byte b = source[i];
			if (Character.isSpaceChar((char) b)) {
				i++;
				continue;
			}
			while (j < prefix.length) {
				if (source[i] != prefix[j]) {
					return false;
				}
				j++;
			}
			return true;
		}
		return false;
	}
	
	public boolean contain(byte[] dst) {
		if (source.length < dst.length) {
			return false;
		}
		byte first = dst[0];
		int i = 0;
		while (true) {
			if (i >= source.length) {
				break;
			}
			if (first == source[i]) {
				int j = 0;
				while (source[++i] == dst[++j]) {
					if (j == dst.length - 1) {
						return true;
					}
				}
			}
			i++;
		}

		return false;
	}

}
