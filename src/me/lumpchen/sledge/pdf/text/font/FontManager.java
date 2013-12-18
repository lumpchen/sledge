package me.lumpchen.sledge.pdf.text.font;

import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class FontManager {

	private static final FontManager instance = new FontManager();
	private Map<FontIndex, PDFFont> fontPool;
	
	private FontManager() {
		this.fontPool = new HashMap<FontIndex, PDFFont>();
	}

	public static FontManager instance() {
		return instance;
	}
	
	public void put(PName name, PDFFont obj) {
		this.fontPool.put(new FontIndex(name), obj);
	}
	
	public PDFFont findFont(FontIndex index) {
		return this.fontPool.get(index);
	}
}
