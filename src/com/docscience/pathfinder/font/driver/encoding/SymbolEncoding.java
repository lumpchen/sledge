package com.docscience.pathfinder.font.driver.encoding;

/**
 * @author wxin
 *
 */
public class SymbolEncoding implements Encoding {

	@Override
	public String getCharacterName(int codepoint) {
		return "S" + codepoint;
	}

	@Override
	public String getGlyphName(int codepoint) {
		return getCharacterName(codepoint);
	}

	@Override
	public int getCodePoint(int unicode) {
		return unicode & 0x0fff;
	}

	@Override
	public int getMaxCodePoint() {
		return 255;
	}

	@Override
	public int getMinCodePoint() {
		return 0;
	}

	@Override
	public int getUnicode(int codepoint) {
		return codepoint | 0xf000;
	}

	@Override
	public boolean isDefinedCodePoint(int codepoint) {
		return true;
	}

}
