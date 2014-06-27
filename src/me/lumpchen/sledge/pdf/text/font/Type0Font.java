package me.lumpchen.sledge.pdf.text.font;

import java.io.IOException;

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
		this.descendantFont.setEncoding(this.encoding);
	}
	
	@Override
	public void renderText(String s, VirtualGraphics gd) throws IOException {
		char[] chars = s.toCharArray();
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			char c0 = chars[i];
			char c1 = chars[++i];
			
			char c = (char) (((c0 << 8)) | (c1 & 0xFF));
			buf.append(c);
		}
		
		this.descendantFont.renderText(buf.toString(), gd);
	}

	@Override
	public void close() {
		
	}
}
