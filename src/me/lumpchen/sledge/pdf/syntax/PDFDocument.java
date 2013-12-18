package me.lumpchen.sledge.pdf.syntax;

import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.document.Catalog;
import me.lumpchen.sledge.pdf.syntax.document.DocumentInfo;
import me.lumpchen.sledge.pdf.syntax.document.Page;
import me.lumpchen.sledge.pdf.syntax.document.PageTree;
import me.lumpchen.sledge.pdf.text.font.FontManager;
import me.lumpchen.sledge.pdf.text.font.PDFFont;

public class PDFDocument {

	private PageContentsLoader pageContentsLoader;

	private FontManager fontManager = FontManager.instance();
	private ResourceManager resourceManager =  ResourceManager.instance();
	private Map<IndirectRef, IndirectObject> objectCache;
	
	private Trailer trailer;
	private XRef xref;
	private DocumentInfo info;
	private Catalog catalog;
	private PageTree rootPageTree;
	
	public PDFDocument() {
		this.objectCache = new HashMap<IndirectRef, IndirectObject>();
	}

	public void setTrailer(Trailer trailer) {
		this.trailer = trailer;
	}

	public Trailer getTrailer() {
		return this.trailer;
	}

	public void setXRef(XRef xref) {
		this.xref = xref;
	}

	public XRef getXRef() {
		return this.xref;
	}

	public void setDocumentInfo(DocumentInfo info) {
		this.info = info;
		this.info.setDocument(this);
	}

	public DocumentInfo getInfo() {
		return this.info;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
		this.catalog.setDocument(this);
	}

	public Catalog getCatalog() {
		return this.catalog;
	}

	public void setRootPageTree(PageTree rootPageTree) {
		this.rootPageTree = rootPageTree;
		this.rootPageTree.setDocument(this);
	}

	public PageTree getRootPageTree() {
		return this.rootPageTree;
	}

	public Page getPage(int pageNo) {
		Page page = this.rootPageTree.getPage(pageNo);
		return page;
	}

	public void setPageContentsLoader(PageContentsLoader pageContentsLoader) {
		this.pageContentsLoader = pageContentsLoader;
	}
	
	public IndirectObject getObject(IndirectRef ref) {
		if (this.objectCache.containsKey(ref)) {
			return this.objectCache.get(ref);
		}
		
		if (this.pageContentsLoader != null) {
			IndirectObject obj = this.pageContentsLoader.loadObject(ref, this);
			this.objectCache.put(ref, obj);
			return obj;
		}
		
		return null;
	}
	
	public void putResource(PName key, PDFFont font) {
		this.fontManager.put(key, font);
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();

		if (this.rootPageTree != null) {
			buf.append(this.rootPageTree.toString());
		}
		buf.append('\n');
		if (this.catalog != null) {
			buf.append(this.catalog.toString());
		}
		buf.append('\n');
		if (this.info != null) {
			buf.append(this.info.toString());
		}
		buf.append('\n');
		if (this.xref != null) {
			buf.append(this.xref.toString());
		}
		buf.append('\n');
		if (this.trailer != null) {
			buf.append(this.trailer.toString());
		}
		buf.append('\n');

		return buf.toString();
	}
}
