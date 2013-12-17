package me.lumpchen.sledge.pdf.syntax;

import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class ResourceManager {

	private Map<PName, IndirectObject> global; 
	
	private ResourceManager() {
		this.global = new HashMap<PName, IndirectObject>();
	}

	public static ResourceManager instance() {
		return new ResourceManager();
	}
	
	public void put(PName name, IndirectObject obj) {
		this.global.put(name, obj);
	}
	
	public IndirectObject get(PName name) {
		return this.global.get(name);
	}
}
