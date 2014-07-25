package me.lumpchen.sledge.pdf.text.font;

import me.lumpchen.sledge.pdf.syntax.lang.PStream;

public class FontFile {
	
	private byte[] fontBytes;
	
	public FontFile(PStream stream) {
		this.fontBytes = stream.getDecodedStream();
	}
	
	public byte[] getBytes() {
		return this.fontBytes;
	}
}
