package com.docscience.pathfinder.font.driver.ps;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.FontException;

/**
 * <pre>
 * Interface for output a font in PS format. It can be used to define/embedding fonts
 * in PS files or generate Type1/CIDFont files. 
 * 
 * There are three parts contained in a PS font.
 * 1. A PS dictionary object contains font informations called font dictionary.
 * 2. A encoding scheme to encode characters to show string. 
 * 3. A glyph description for each glyph.
 * 
 * We abstract encoding scheme to following concepts:
 * 1. unicode: a character.
 * 2. code point: a character's encoded value, each unicode may have or haven't a code point, 
 * desided by encoding scheme
 * 3. glyph id: a value index the glyph in fonts. every code point may match to a glyph id.
 * 4. dependent glyph id: a glyph may composed by other glyphs (sub glyphs), the dependent 
 * glyph id defines all the decent glyphs' id and its self id. 
 * 
 * ShowString is used for "show" command in PS.
 * </pre>
 * 
 * @author wxin
 */
public interface PSFontOutput {
	
	public PSFontDictionary getFontDictionary() throws FontException, IOException;
	
	public int getCodePoint(int unicode) throws FontException, IOException;
	
	public int getGlyphID(int codepoint) throws FontException, IOException;
	
	public PSName getGlyphName(int glyphID) throws FontException, IOException;
	
	public PSObject getGlyphPresentation(int glyphID) throws FontException, IOException;
	
	public PSString getShowString(String string) throws FontException, IOException;
	
}
