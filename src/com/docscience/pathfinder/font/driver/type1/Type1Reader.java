package com.docscience.pathfinder.font.driver.type1;

import java.io.File;
import java.io.IOException;

import com.docscience.pathfinder.font.driver.AbstractFontReader;
import com.docscience.pathfinder.font.driver.cff.CFFRandomReader;
import com.docscience.pathfinder.font.driver.cff.CFFRandomReaderWrapper;
import com.docscience.pathfinder.font.driver.cff.CFFWrapper;
import com.docscience.pathfinder.font.driver.ttf.TTFRandomArrayReader;
import com.docscience.pathfinder.font.driver.ttf.TTFRandomFileReader;
import com.docscience.pathfinder.font.driver.ttf.TTFRandomReader;
import com.docscience.pathfinder.font.shared.FontDictionary;
import com.docscience.pathfinder.font.shared.FontEncoding;
import com.docscience.pathfinder.font.shared.FontMetrics;
import com.docscience.pathfinder.font.shared.FontTechnology;
import com.docscience.pathfinder.font.shared.GlyphDescription;

public class Type1Reader extends AbstractFontReader {
	
	private TTFRandomReader ttfRandom;
	private CFFRandomReader cffRandomReader;
	private CFFWrapper cffWrapper;
	
	public Type1Reader(File file) throws IOException {
		try {
			this.ttfRandom = new TTFRandomFileReader(file);
			this.cffRandomReader = new CFFRandomReaderWrapper(ttfRandom, 0, file.length());
			this.cffWrapper = new CFFWrapper(this.cffRandomReader);
		} catch (Throwable e) {
			if (this.ttfRandom != null) {
				this.ttfRandom.close();
			}
			throw new IOException(e);
		}
	}

	public Type1Reader(byte[] data) throws IOException {
		try {
			this.ttfRandom = new TTFRandomArrayReader(data);
			this.cffRandomReader = new CFFRandomReaderWrapper(ttfRandom, 0, data.length);
			this.cffWrapper = new CFFWrapper(this.cffRandomReader);
		} catch (Throwable e) {
			if (this.ttfRandom != null) {
				this.ttfRandom.close();
			}
			throw new IOException(e);
		}
	}

	@Override
	public void close() throws IOException {
		this.ttfRandom = null;
	}

	@Override
	public int getNumFonts() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void selectFont(int index) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getFontFamily() throws IOException {
		// TODO Auto-generated method stub
		return this.cffWrapper.getFamilyName();
	}

	@Override
	public String getFontStyle() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontTechnology getTechnology() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontDictionary[] getDictionaries() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontEncoding getEncoding() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontMetrics getMetrics() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGlyphCount() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public GlyphDescription getGlyph(int glyphId) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
