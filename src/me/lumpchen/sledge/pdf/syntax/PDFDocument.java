package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.syntax.document.Catalog;
import me.lumpchen.sledge.pdf.syntax.document.DocumentInfo;
import me.lumpchen.sledge.pdf.syntax.document.Page;
import me.lumpchen.sledge.pdf.syntax.document.PageTree;

public class PDFDocument {

	private PageContentsLoader pageContentsLoader;

	private Trailer trailer;
	private XRef xref;
	private DocumentInfo info;
	private Catalog catalog;
	private PageTree rootPageTree;
	
	public PDFDocument() {
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
	}

	public DocumentInfo getInfo() {
		return this.info;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	public Catalog getCatalog() {
		return this.catalog;
	}

	public void setRootPageTree(PageTree rootPageTree) {
		this.rootPageTree = rootPageTree;
	}

	public PageTree getRootPageTree() {
		return this.rootPageTree;
	}

	public Page getPage(int pageNo) {
		Page page = this.rootPageTree.getPage(pageNo);
		if (null == page) {
			return null;
		}

		if (null == page.getContents()) {
			this.pageContentsLoader.loadPageContents(page, this);
		}
		
		this.pageContentsLoader.loadPageResource(page, this);
		return page;
	}

	public void setPageContentsLoader(PageContentsLoader pageContentsLoader) {
		this.pageContentsLoader = pageContentsLoader;
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
