package me.lumpchen.sledge.pdf.text.font;

import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.document.FontObject;

public class Type1Font extends PDFFont {

	public Type1Font(FontObject fontObj) {
		super(fontObj);
	}

	@Override
	public void renderText(char[] c, VirtualGraphics gd) {
		// TODO Auto-generated method stub
	}
}
