package com.docscience.pathfinder.font.driver;

import java.io.IOException;

import com.docscience.pathfinder.font.shared.Font;
import com.docscience.pathfinder.font.shared.FontDictionary;
import com.docscience.pathfinder.font.shared.FontLoadOptions;

public abstract class AbstractFontReader implements FontReader {

	@Override
	public final Font getFont(FontLoadOptions options) throws IOException {
		Font font = new Font();
		
		font.setTechnology(this.getTechnology());
		
		for (FontDictionary names : this.getDictionaries()) {
			font.addDictionary(names);
		}
		
		if (options.isLoadEncoding()) {
			font.setEncoding(this.getEncoding());
		}
		
		if (options.isLoadMetrics()) {
			font.setMetrics(this.getMetrics());
		}
		
		if (options.isLoadUnmappedGlyphs()) {
			// TODO: ADD CODE HERE
		}
		
		return font;
	}
	
}
