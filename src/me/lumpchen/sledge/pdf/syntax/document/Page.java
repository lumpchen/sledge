package me.lumpchen.sledge.pdf.syntax.document;

import java.util.List;

import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.graphics.VirtualGraphicsHelper;
import me.lumpchen.sledge.pdf.reader.ContentStreamReader;
import me.lumpchen.sledge.pdf.syntax.ContentStream;
import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.Resource;
import me.lumpchen.sledge.pdf.syntax.SyntaxException;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.syntax.basic.PStream;
import me.lumpchen.sledge.pdf.syntax.basic.Rectangle;
import me.lumpchen.sledge.pdf.text.font.PDFFont;

public class Page extends DocObject {

	private int pageNo;

	private boolean resourceLoaded = false;
	private IndirectObject contentStreamObj;
	
	private Rectangle mediaBox;
	private Rectangle cropBox;
	private Rectangle bleedBox;
	private Rectangle trimBox;
	private Rectangle artBox;
	
	private double width;
	private double height;
	
	public Page(IndirectObject obj, PDFDocument owner) {
		super(obj, owner);
		
		PArray rect = super.getValueAsArray(PName.MediaBox);
		this.mediaBox = new Rectangle(rect);
		this.width = this.mediaBox.getWidth();
		this.height = this.mediaBox.getHeight();
	}
	
	public PName getType() {
		return PName.Page;
	}
	
	public IndirectRef getContentsRef() {
		return this.getValueAsRef(PName.Contents);
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	public int getPageNo() {
		return this.pageNo;
	}

	public double getWidth() {
		return this.width;
	}
	
	public double getHeight() {
		return this.height;
	}
	
	public Rectangle getMediaBox() {
		return mediaBox;
	}

	public void setMediaBox(Rectangle mediaBox) {
		this.mediaBox = mediaBox;
		this.width = this.mediaBox.getWidth();
		this.height = this.mediaBox.getHeight();
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
		if (null == this.contentStreamObj) {
			IndirectRef contentRef = this.getContentsRef();
			if (null == contentRef) {
				return null;
			}
			this.contentStreamObj = this.owner.getObject(contentRef);
		}
		
		PStream stream = this.contentStreamObj.getStream();
		if (null == stream) {
			return null;
		}
		
		byte[] bstream = stream.getStream();
		if (null == bstream || bstream.length <= 0) {
			return null;
		}
		
		ContentStreamReader csReader = new ContentStreamReader();
		ContentStream contentStream = csReader.read(bstream);
		
		System.out.println(contentStream);
		
		return contentStream;
	}
	
	public void render(VirtualGraphics g2) {
		this.loadResource();
		
		Rectangle mediaBox = this.getMediaBox();
		VirtualGraphicsHelper.drawRectangle(mediaBox, g2);
		
		g2.beginCanvas(mediaBox.getWidth(), mediaBox.getHeight());
		
		ContentStream cs = this.getContentStream();
		cs.render(g2, this);
	}
	
	public PObject getResources() {
		return super.getValueAsDict(PName.Resources);
	}
	
	public PDFFont getFont(PName name) {
		return this.owner.getFont(new FontIndex(name));
	}
	
	private void loadResource() {
		if (this.resourceLoaded) {
			return;
		}
		PObject res = this.getResources();
		if (null == res) {
			return;
		}
		if (res instanceof IndirectRef) {
			IndirectRef resRef = (IndirectRef) res;
			IndirectObject resObj = this.owner.getObject(resRef);
			PDictionary dict = resObj.getDict();
			if (null != dict) {
				Resource resource = new Resource(dict);
				this.loadResource(resource);
			}
		} else if (res instanceof PDictionary) {
			Resource resource = new Resource((PDictionary) res);
			this.loadResource(resource);
		}
		this.resourceLoaded = true;
	}
	
	private void loadResource(Resource res) {
		this.loadFont(res.getFont());
	}
	
	private void loadFont(PDictionary fontDict) {
		if (null == fontDict || fontDict.isEmpty()) {
			return;
		}
		List<PName> keys = fontDict.keyList();
		for (PName key : keys) {
			PObject obj = fontDict.get(key);
			if (obj instanceof IndirectRef) {
				IndirectObject resObj = this.owner.getObject((IndirectRef) obj);
				if (null != resObj) {
					PName type = resObj.getValueAsName(PName.Type);
					if (null == type || !type.equals(PName.Font)) {
						throw new SyntaxException("not a font object");
					}
					FontIndex fontIndex = new FontIndex(key);
					FontObject fontObj = new FontObject(resObj, this.owner);
					this.owner.putResource(fontIndex, fontObj);
				} else {
					throw new SyntaxException("null object");
				}
			}
		}
	}
}
