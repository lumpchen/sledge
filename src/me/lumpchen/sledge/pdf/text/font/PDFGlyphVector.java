package me.lumpchen.sledge.pdf.text.font;

import me.lumpchen.sledge.pdf.graphics.PDFColor;

public class PDFGlyphVector {

	private char[] text;
	private PDFColor color;
	private PDFFont font;

	public PDFGlyphVector(PDFFont font) {
		this.font = font;
		
	}

	public void setText(char[] text) {
		this.text = text;
	}

	public void setColor(PDFColor color) {
		this.color = color;
	}
	
	public PDFGlyph[] getGlyphs() {
		return null;
	}
	
	public PDFGlyph getGlyph(char c) {
		return null;
	}

}
