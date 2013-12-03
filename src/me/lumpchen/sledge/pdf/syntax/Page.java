package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.Rectangle;

public class Page extends DocObject {

	private int pageNo;

	private IndirectObject streamObj;
	
	private Rectangle mediaBox;
	private Rectangle cropBox;
	private Rectangle bleedBox;
	private Rectangle trimBox;
	private Rectangle artBox;
	
	public Page(IndirectObject obj) {
		super(obj);
	}
	
	public PName getType() {
		return PName.page;
	}
	
	public IndirectRef getContentsRef() {
		return this.getValueAsRef(PName.contents);
	}
	
	public void setContents(IndirectObject streamObj) { 
		this.streamObj = streamObj;
	}
	
	public IndirectObject getContents() {
		return this.streamObj;
	}
	
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	public int getPageNo() {
		return this.pageNo;
	}

	public Rectangle getMediaBox() {
		return mediaBox;
	}

	public void setMediaBox(Rectangle mediaBox) {
		this.mediaBox = mediaBox;
	}

	public Rectangle getCropBox() {
		return cropBox;
	}

	public void setCropBox(Rectangle cropBox) {
		this.cropBox = cropBox;
	}

	public Rectangle getBleedBox() {
		return bleedBox;
	}

	public void setBleedBox(Rectangle bleedBox) {
		this.bleedBox = bleedBox;
	}

	public Rectangle getTrimBox() {
		return trimBox;
	}

	public void setTrimBox(Rectangle trimBox) {
		this.trimBox = trimBox;
	}

	public Rectangle getArtBox() {
		return artBox;
	}

	public void setArtBox(Rectangle artBox) {
		this.artBox = artBox;
	}
}
