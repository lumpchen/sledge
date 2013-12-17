package me.lumpchen.sledge.pdf.text.font;

public class FontManager {

	private FontManager() {
	}

	public static FontManager instance() {
		return new FontManager();
	}

	public PDFFont findFont(FontIndex index) {
		return null;
	}
}
