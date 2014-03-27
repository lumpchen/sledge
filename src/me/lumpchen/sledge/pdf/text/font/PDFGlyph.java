package me.lumpchen.sledge.pdf.text.font;

import java.awt.geom.GeneralPath;

public class PDFGlyph {

	private GeneralPath gpath;
	
	public PDFGlyph() {
	}
	
	public void setGlyph(GeneralPath path) {
		this.gpath = path;
	}
	
	public GeneralPath getGlyph() {
		return this.gpath;
	}
}
