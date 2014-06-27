package com.docscience.pathfinder.font.driver;

import java.io.File;
import java.io.IOException;

import com.docscience.pathfinder.font.driver.ttf.TTFReader;

public class FontReaderFactory {

	private static FontReaderFactory instance = new FontReaderFactory();
	
	public static FontReaderFactory getInstance() {
		return instance;
	}

	public FontReader createFontReader(File file) throws IOException {
		return new TTFReader(file);
	}

	public FontReader createFontReader(byte[] data) throws IOException {
		return new TTFReader(data);
	}
	
}
