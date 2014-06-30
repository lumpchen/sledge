package com.docscience.pathfinder.font.driver;

import java.io.File;
import java.io.IOException;

import com.docscience.pathfinder.font.driver.ttf.TTFReader;
import com.docscience.pathfinder.font.driver.type1.Type1Reader;

public class FontReaderFactory {

	private static FontReaderFactory instance = new FontReaderFactory();

	public enum Type {
		TrueType, Type1
	};

	public static FontReaderFactory getInstance() {
		return instance;
	}

	public FontReader createFontReader(File file, Type type) throws IOException {
		if (type == Type.TrueType) {
			return new TTFReader(file);
		} else if (type == Type.Type1) {
			return new Type1Reader(file);
		}
		return null;
	}

	public FontReader createFontReader(byte[] data, Type type) throws IOException {
		if (type == Type.TrueType) {
			return new TTFReader(data);
		} else if (type == Type.Type1) {
			return new Type1Reader(data);
		}
		return null;
	}

}
