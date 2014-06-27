package com.docscience.pathfinder.font.driver;

import java.io.IOException;

import com.docscience.pathfinder.font.shared.Font;
import com.docscience.pathfinder.font.shared.FontDictionary;
import com.docscience.pathfinder.font.shared.FontEncoding;
import com.docscience.pathfinder.font.shared.FontLoadOptions;
import com.docscience.pathfinder.font.shared.FontMetrics;
import com.docscience.pathfinder.font.shared.FontTechnology;
import com.docscience.pathfinder.font.shared.GlyphDescription;

public interface FontReader {

	void close() throws IOException;

	int getNumFonts() throws IOException;
	
	void selectFont(int index) throws IOException;
	
	String getFontFamily() throws IOException;
	
	String getFontStyle() throws IOException;
		
	FontTechnology getTechnology() throws IOException;
	
	FontDictionary[] getDictionaries() throws IOException;
	
	FontEncoding getEncoding() throws IOException;
		
	FontMetrics getMetrics() throws IOException;
	
	int getGlyphCount() throws IOException;
	
	GlyphDescription getGlyph(int glyphId) throws IOException;
	
	Font getFont(FontLoadOptions options) throws IOException;
	
}
