package com.docscience.pathfinder.font.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("serial")
public final class FontGroup implements Serializable {

	private String fontFamily;

	private FontTechnology technology;

	private ArrayList<FontEntry> entries = new ArrayList<FontEntry>();

	public FontGroup() {
		// for GWT serialize
	}

	public FontGroup(String fontFamily, FontTechnology technology) {
		if (fontFamily == null) {
			throw new IllegalArgumentException("fontFamily=" + fontFamily);
		}
		if (technology == null) {
			throw new IllegalArgumentException("technology=" + technology);
		}
		this.fontFamily = fontFamily;
		this.technology = technology;
	}

	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		if (fontFamily == null) {
			throw new IllegalArgumentException("fontFamily=" + fontFamily);
		}
		this.fontFamily = fontFamily;
	}

	public FontTechnology getTechnology() {
		return technology;
	}

	public void setTechnology(FontTechnology technology) {
		if (technology == null) {
			throw new IllegalArgumentException("technology=" + technology);
		}
		this.technology = technology;
	}

	public List<FontEntry> getEntries() {
		return Collections.unmodifiableList(entries);
	}

	public int getEntryCount() {
		return entries.size();
	}
	
	public int getEntryIndex(FontEntry entry) {
		return entries.indexOf(entry);
	}

	public FontEntry getEntry(int index) {
		return entries.get(index);
	}

	public FontEntry getEntry(String fontStyle) {
		for (int i = 0, len = entries.size(); i < len; i++) {
			FontEntry entry = entries.get(i);
			if (entry.getFontStyle().equals(fontStyle)) {
				return entry;
			}
		}
		return null;
	}

	public void addEntry(FontEntry entry) {
		entry.setFontGroup(this);
		entries.add(entry);
	}
	
	public FontEntry getDefaultEntry() {
		FontEntry entry = getEntry("Regular");
		if (entry == null) {
			entry = getEntry("Roman");
			if (entry == null) {
				entry = getEntry("Normal");
				if (entry == null) {
					entry = entries.get(0);
					if (entry == null) {
						throw new RuntimeException("cannot find any font entry");
					}
				}
			}
		}
		return entry;
	}

	@Override
	public String toString() {
		return "FontGroup [fontFamily=" + fontFamily + ", technology="
				+ technology + ", entries=" + entries + "]";
	}

}
