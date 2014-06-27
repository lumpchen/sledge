package com.docscience.pathfinder.font.driver.encoding;

/**
 * @author wxin
 *
 */
public interface Encoding {
	
	public int getMaxCodePoint();
	
	public int getMinCodePoint();
	
	public int getCodePoint(int unicode);
	
	public String getCharacterName(int codepoint);

	public String getGlyphName(int codepoint);
	
	public int getUnicode(int codepoint);
	
	public boolean isDefinedCodePoint(int codepoint);
}
