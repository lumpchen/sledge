package com.docscience.pathfinder.font.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FontEntry implements Serializable {

	private FontGroup fontGroup;

	private String fontStyle;

	private long fontId;

	public FontEntry() {
		// for GWT serialize
	}

	public FontEntry(String fontStyle, long fontId) {
		if (fontStyle == null) {
			throw new IllegalArgumentException("fontStyle=" + fontStyle);
		}
		this.fontStyle = fontStyle;
		this.fontId = fontId;
	}

	public FontGroup getFontGroup() {
		return fontGroup;
	}

	void setFontGroup(FontGroup fontGroup) {
		this.fontGroup = fontGroup;
	}

	public String getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}

	public long getFontId() {
		return fontId;
	}

	public void setFontId(long fontId) {
		this.fontId = fontId;
	}

	@Override
	public String toString() {
		return "FontEntry [fontStyle=" + fontStyle + ", fontId=" + fontId + "]";
	}

}
