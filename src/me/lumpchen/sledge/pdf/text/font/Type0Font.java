package me.lumpchen.sledge.pdf.text.font;

import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.document.FontObject;

public class Type0Font extends PDFFont {

	private PDFFont descendantFont;
	
	public Type0Font(FontObject fontObj) {
		super(fontObj);
		
		this.initDescendantFont();
	}

	private void initDescendantFont() {
		this.descendantFont = PDFFont.create(this.descendantFontObj);
	}
	
	@Override
	public void renderText(char[] c, VirtualGraphics gd) {
		this.descendantFont.renderText(c, gd);		
	}
}
