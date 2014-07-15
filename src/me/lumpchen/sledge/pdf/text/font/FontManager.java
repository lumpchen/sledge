package me.lumpchen.sledge.pdf.text.font;

import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.syntax.document.FontObject;

public class FontManager {

	private static final FontManager instance = new FontManager();
	private Map<String, PDFFont> fontPool;
	
	private FontManager() {
		this.fontPool = new HashMap<String, PDFFont>();
	}

	public static FontManager instance() {
		return instance;
	}
	
	public PDFFont getFont(FontObject fo) {
		if (fo == null) {
			return null;
		}
		
//		String key = this.keyedFontObject(fo);
//		if (this.fontPool.containsKey(key)) {
//			return this.fontPool.get(key);
//		} else {
			PDFFont font = this.loadFont(fo);
//			this.fontPool.put(key, font);
			return font;
//		}
	}
	
	private PDFFont loadFont(FontObject fo) {
		return PDFFont.create(fo);
	}
	
	private String keyedFontObject(FontObject fo) {
		StringBuilder buf = new StringBuilder();
		buf.append(fo.getBaseFont().getName());
		buf.append(fo.getSubType().getName());
		return buf.toString();
	}
	
}
