package me.lumpchen.sledge.pdf.text.font;

import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

public class PDFGlyph {

	private int cid;
	private int gid;
	
	private double advance;
	private GeneralPath gpath;
	private BufferedImage bitmap;
	
	public PDFGlyph() {
	}
	
	public void setGlyph(GeneralPath path) {
		this.gpath = path;
	}
	
	public GeneralPath getGlyph() {
		return this.gpath;
	}

	public BufferedImage getBitmap() {
		return this.bitmap;
	}
	
	public void setBitmap(BufferedImage bitmap) {
		this.bitmap = bitmap;
	}
	
	public void setAdvance(double advance) {
		this.advance = advance;
	}
	
	public double getAdvance() {
		return this.advance;
	}
}
