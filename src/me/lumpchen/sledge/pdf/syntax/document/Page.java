package me.lumpchen.sledge.pdf.syntax.document;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.graphics.VirtualGraphicsHelper;
import me.lumpchen.sledge.pdf.reader.ContentStreamReader;
import me.lumpchen.sledge.pdf.syntax.ContentStream;
import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.Resource;
import me.lumpchen.sledge.pdf.syntax.SyntaxException;
import me.lumpchen.sledge.pdf.syntax.lang.PArray;
import me.lumpchen.sledge.pdf.syntax.lang.PDictionary;
import me.lumpchen.sledge.pdf.syntax.lang.PName;
import me.lumpchen.sledge.pdf.syntax.lang.PObject;
import me.lumpchen.sledge.pdf.syntax.lang.PStream;
import me.lumpchen.sledge.pdf.syntax.lang.Rectangle;
import me.lumpchen.sledge.pdf.text.font.FontManager;
import me.lumpchen.sledge.pdf.text.font.PDFFont;

public class Page extends DocObject {

	private int pageNo;

	private boolean resourceLoaded = false;
	private ContentStream contentStream;

	private Rectangle mediaBox;
	private Rectangle cropBox;
	private Rectangle bleedBox;
	private Rectangle trimBox;
	private Rectangle artBox;

	private double width;
	private double height;

	private Map<FontIndex, FontObject> indexedFontMap;
	private FontManager fontManager = FontManager.instance();

	public Page(IndirectObject obj, PDFDocument owner) {
		super(obj, owner);

		this.indexedFontMap = new HashMap<FontIndex, FontObject>();
		PArray rect = super.getValueAsArray(PName.MediaBox);
		this.mediaBox = new Rectangle(rect);
		this.width = this.mediaBox.getWidth();
		this.height = this.mediaBox.getHeight();
	}

	public PName getType() {
		return PName.Page;
	}

	public PStream getContents() {
		PObject obj = this.getValue(PName.Contents);
		if (obj == null) {
			return null;
		}
		
		if (obj.getClassType() == PObject.ClassType.IndirectRef) {
			return this.getContents((IndirectRef) obj);
		} else if (obj.getClassType() == PObject.ClassType.Array) {
			PStream contents = null;
			PArray arr = (PArray) obj;
			for (int i = 0; i < arr.size(); i++) {
				PObject item = arr.get(i);
				if (item.getClassType() == PObject.ClassType.IndirectRef) {
					PStream si = this.getContents((IndirectRef) item);
					
					if (contents == null) {
						//need clone dict here?
						contents = new PStream(si.getDict(), si.getStream());
					} else {
						byte[] s1 = contents.getStream();
						byte[] s2 = si.getStream();
						
						byte[] ss = new byte[s1.length + s2.length];
						System.arraycopy(s1, 0, ss, 0, s1.length);
						System.arraycopy(s2, 0, ss, s1.length, s2.length);
						contents.setStream(ss);
					}
				}
			}
			return contents;
		}
		
		return null;
	}
	
	private PStream getContents(IndirectRef ref) {
		IndirectObject obj = this.owner.getObject(ref);
		if (obj != null && obj.getStream() != null) {
			return obj.getStream();
		}
		return null;
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
		if (this.contentStream != null) {
			return this.contentStream;
		}
		
		PStream stream = this.getContents();
		if (null == stream) {
			return null;
		}

		byte[] bstream = stream.getDecodedStream();
		if (null == bstream || bstream.length <= 0) {
			return null;
		}

		System.err.println(stream);

		ContentStreamReader csReader = new ContentStreamReader();
		contentStream = csReader.read(bstream);

		System.out.println(contentStream);

		return contentStream;
	}

	public void render(VirtualGraphics g2) {
		this.loadResource();

		Rectangle mediaBox = this.getMediaBox();
		VirtualGraphicsHelper.drawRectangle(mediaBox, g2);

		g2.beginCanvas(mediaBox.getWidth(), mediaBox.getHeight());

		ContentStream cs = this.getContentStream();
		if (cs != null) {
			cs.render(g2, this);
		}
	}

	public PObject getResources() {
		return super.getValueAsDict(PName.Resources);
	}

	private FontIndex lastFontIndex;
	private PDFFont lastPDFFont;

	public PDFFont getFont(PName name) {
		FontIndex fontIndex = new FontIndex(name);
		if (!this.indexedFontMap.containsKey(fontIndex)) {
			throw new SyntaxException("not found the font: " + fontIndex.toString());
		}

		if (fontIndex.equals(this.lastFontIndex)) {
			return this.lastPDFFont;
		} else {
			if (this.lastPDFFont != null) {
				try {
					this.lastPDFFont.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		FontObject fontObj = this.indexedFontMap.get(fontIndex);
		this.lastPDFFont = this.fontManager.getFont(fontObj);
		return this.lastPDFFont;
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
					this.indexedFontMap.put(fontIndex, fontObj);
				} else {
					throw new SyntaxException("null object");
				}
			}
		}
	}
}
