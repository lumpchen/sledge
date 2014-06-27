package com.docscience.pathfinder.font.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FontLoadOptions implements Serializable {
	
	private boolean loadEncoding;

	private boolean loadMetrics;

	private boolean loadUnmappedGlyphs;

	public FontLoadOptions() {
		// do nothing here
	}
	
	public FontLoadOptions(boolean loadEncoding, boolean loadMetrics,
			boolean loadUnmappedGlyphs) {
		this.loadEncoding = loadEncoding;
		this.loadMetrics = loadMetrics;
		this.loadUnmappedGlyphs = loadUnmappedGlyphs;
	}

	public boolean isLoadEncoding() {
		return loadEncoding;
	}

	public void setLoadEncoding(boolean b) {
		this.loadEncoding = b;
	}

	public boolean isLoadMetrics() {
		return loadMetrics;
	}

	public void setLoadMetrics(boolean b) {
		this.loadMetrics = b;
	}

	public boolean isLoadUnmappedGlyphs() {
		return loadUnmappedGlyphs;
	}

	public void setLoadUnmappedGlyphs(boolean b) {
		this.loadUnmappedGlyphs = b;
	}

	public boolean isMatched(Font font) {
		if (isLoadEncoding() && font.getEncoding() == null) {
			return false;
		} else if (isLoadMetrics() && font.getMetrics() == null) {
			return false;
		} else if (isLoadUnmappedGlyphs() && font.getGlyphs().isEmpty()) {
			return false;
		}
		return true;
	}

}
