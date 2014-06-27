package com.docscience.pathfinder.font.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GlyphDescription implements Serializable {

	private int id;

	private GlyphPath path;

	public GlyphDescription() {
		// do nothing here
	}

	public GlyphDescription(int id, GlyphPath path) {
		this.id = id;
		this.path = path;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public GlyphPath getPath() {
		return this.path;
	}

	public void setPath(GlyphPath path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "GlyphDescription [id=" + id + ", path=" + path + "]";
	}

}
