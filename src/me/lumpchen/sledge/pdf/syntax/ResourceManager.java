package me.lumpchen.sledge.pdf.syntax;

import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.syntax.lang.PName;
import me.lumpchen.sledge.pdf.text.font.PDFFont;

public class ResourceManager {

	private static final ResourceManager instance = new ResourceManager(); 
	private Map<PName, PDFFont> fontPool;
	
	private ResourceManager() {
		this.fontPool = new HashMap<PName, PDFFont>();
	}

	public static ResourceManager instance() {
		return instance;
	}
	
	public void put(PName name, PDFFont obj) {
		this.fontPool.put(name, obj);
	}
	
	public PDFFont get(PName name) {
		return this.fontPool.get(name);
	}
}
