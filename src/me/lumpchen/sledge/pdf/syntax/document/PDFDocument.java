package me.lumpchen.sledge.pdf.syntax.document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.ObjectStream;
import me.lumpchen.sledge.pdf.syntax.PageContentsLoader;
import me.lumpchen.sledge.pdf.syntax.ResourceManager;
import me.lumpchen.sledge.pdf.syntax.Trailer;
import me.lumpchen.sledge.pdf.syntax.XRef;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.syntax.decrypt.EncryptionUnsupportedByPlatformException;
import me.lumpchen.sledge.pdf.syntax.decrypt.EncryptionUnsupportedByProductException;
import me.lumpchen.sledge.pdf.syntax.decrypt.PDFAuthenticationFailureException;
import me.lumpchen.sledge.pdf.syntax.decrypt.PDFDecryptException;
import me.lumpchen.sledge.pdf.syntax.decrypt.PDFDecrypter;
import me.lumpchen.sledge.pdf.syntax.decrypt.PDFDecrypterFactory;
import me.lumpchen.sledge.pdf.syntax.decrypt.PDFPassword;

public class PDFDocument {

	private PageContentsLoader pageContentsLoader;

	private ResourceManager resourceManager = ResourceManager.instance();
	private Map<IndirectRef, IndirectObject> objectCache;
	private Map<IndirectRef, ObjectStream> objStreamCache;

	private Trailer trailer;
	private XRef xref;
	private DocumentInfo info;
	private Catalog catalog;
	private PageTree rootPageTree;
	private int pageCount;
	private PDFDecrypter decrypter;
	private PDFPassword password;

	public PDFDocument() {
		this.objectCache = new HashMap<IndirectRef, IndirectObject>();
		this.objStreamCache = new HashMap<IndirectRef, ObjectStream>();
	}

	public void setTrailer(Trailer trailer) {
		this.trailer = trailer;
	}

	public void checkSecurity(byte[] password) throws PDFAuthenticationFailureException {
		this.password = new PDFPassword(password);
		PObject encrypt = this.trailer.getEncrypt();
		if (encrypt != null) {
			if (encrypt instanceof IndirectRef) {
				IndirectObject encryptObj = this.getObject((IndirectRef) encrypt);
				try {
					this.decrypter = PDFDecrypterFactory.createDecrypter(
							encryptObj, this.trailer.getID(), PDFPassword.nonNullPassword(this.password));
				} catch (EncryptionUnsupportedByProductException
						| EncryptionUnsupportedByPlatformException
						| IOException | PDFDecryptException e) {
					e.printStackTrace();
				}
			}
		}		
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
		this.pageCount = this.rootPageTree.getCount();
	}

	public int getPageCount() {
		return this.pageCount;
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

	public void pushObjStream(IndirectRef ref, ObjectStream stream) {
		this.objStreamCache.put(ref, stream);
	}

	public ObjectStream getObjStream(IndirectRef ref) {
		return this.objStreamCache.get(ref);
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
