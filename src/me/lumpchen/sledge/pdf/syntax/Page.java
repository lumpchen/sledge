package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.graphics.VirtualGraphicsHelper;
import me.lumpchen.sledge.pdf.reader.ContentStreamReader;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PStream;
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
		PArray rect = super.getValueAsArray(PName.mediabox);
		this.mediaBox = new Rectangle(rect);
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
	
	private ContentStream getContentStream() {
		PStream stream = this.getContents().getStream();
		if (null == stream) {
			return null;
		}
		byte[] bstream = stream.getStream();
		if (null == bstream || bstream.length <= 0) {
			return null;
		}
		
		ContentStreamReader csReader = new ContentStreamReader();
		ContentStream contentStream = csReader.read(bstream);
		return contentStream;
	}
	
	public void render(VirtualGraphics g2) {
		Rectangle mediaBox = this.getMediaBox();
		VirtualGraphicsHelper.drawRectangle(mediaBox, g2);
		
		g2.beginCanvas(mediaBox.getWidth(), mediaBox.getHeight());
		
		ContentStream cs = this.getContentStream();
		cs.render(g2);
	}
}
