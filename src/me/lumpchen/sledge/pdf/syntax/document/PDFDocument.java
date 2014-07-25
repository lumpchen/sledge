package me.lumpchen.sledge.pdf.syntax.document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import me.lumpchen.sledge.pdf.reader.PDFAuthenticationFailureException;
import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.ObjectStream;
import me.lumpchen.sledge.pdf.syntax.PageContentsLoader;
import me.lumpchen.sledge.pdf.syntax.ResourceManager;
import me.lumpchen.sledge.pdf.syntax.SyntaxException;
import me.lumpchen.sledge.pdf.syntax.Trailer;
import me.lumpchen.sledge.pdf.syntax.XRef;
import me.lumpchen.sledge.pdf.syntax.ecryption.BadSecurityHandlerException;
import me.lumpchen.sledge.pdf.syntax.ecryption.CryptographyException;
import me.lumpchen.sledge.pdf.syntax.ecryption.DecryptionMaterial;
import me.lumpchen.sledge.pdf.syntax.ecryption.PDEncryptionDictionary;
import me.lumpchen.sledge.pdf.syntax.ecryption.SecurityHandler;
import me.lumpchen.sledge.pdf.syntax.ecryption.SecurityHandlersManager;
import me.lumpchen.sledge.pdf.syntax.ecryption.StandardDecryptionMaterial;
import me.lumpchen.sledge.pdf.syntax.lang.PArray;
import me.lumpchen.sledge.pdf.syntax.lang.PDictionary;
import me.lumpchen.sledge.pdf.syntax.lang.PName;
import me.lumpchen.sledge.pdf.syntax.lang.PObject;
import me.lumpchen.sledge.pdf.syntax.lang.PStream;
import me.lumpchen.sledge.pdf.syntax.lang.PString;

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

	private SecurityHandler securityHandler;

	public PDFDocument() {
		this.objectCache = new HashMap<IndirectRef, IndirectObject>();
		this.objStreamCache = new HashMap<IndirectRef, ObjectStream>();
	}

	public void setTrailer(Trailer trailer) {
		this.trailer = trailer;
		if (this.trailer.getXRefObj() != null) {
			IndirectObject obj = this.trailer.getXRefObj();
			this.objectCache.put(new IndirectRef(obj.getObjNum(), obj.getGenNumb()), obj);
		}
	}

	public void checkSecurity(byte[] password, String keyStore, String alias)
			throws PDFAuthenticationFailureException, BadSecurityHandlerException,
			CryptographyException, IOException {
		PObject obj = this.trailer.getEncrypt();
		if (obj == null) {
			return;
		}

		PDictionary encryptDict;
		if (obj instanceof IndirectRef) {
			encryptDict = this.getObject((IndirectRef) obj).getDict();
		} else if (obj instanceof IndirectObject) {
			encryptDict = ((IndirectObject) obj).getDict();
		} else if (obj instanceof IndirectObject) {
			encryptDict = (PDictionary) obj;
		} else {
			throw new SyntaxException("invalid encrypt object: " + obj);
		}

		DecryptionMaterial decryptionMaterial = null;
		PDEncryptionDictionary encryption = new PDEncryptionDictionary(encryptDict);
		if (encryption.isStandard()) {
			decryptionMaterial = new StandardDecryptionMaterial(password);
			this.securityHandler = SecurityHandlersManager.getInstance().getSecurityHandler(
					encryption.getFilter());
			this.securityHandler.prepareForDecryption(encryption, this.getTrailer().getID(),
					decryptionMaterial);
		}
	}

	public void decrypt(IndirectObject obj) throws CryptographyException, IOException {
		if (this.securityHandler == null) {
			return;
		}
		this.decrpt(obj.getObjNum(), obj.getGenNumb(), obj.insideObj());
	}

	private void decrpt(int objNum, int genNum, PObject obj) throws CryptographyException,
			IOException {
		if (obj.getClassType() == PObject.ClassType.Stream) {
			this.decrpt(objNum, genNum, (PStream) obj);
		} else if (obj.getClassType() == PObject.ClassType.String) {
			this.decrpt(objNum, genNum, (PString) obj);
		} else if (obj.getClassType() == PObject.ClassType.Dict) {
			this.decrpt(objNum, genNum, (PDictionary) obj);
		} else if (obj.getClassType() == PObject.ClassType.Array) {
			this.decrpt(objNum, genNum, (PArray) obj);
		} else {
			return;
		}
	}

	private void decrpt(int objNum, int genNum, PArray arr) throws CryptographyException,
			IOException {
		for (int i = 0; i < arr.size(); i++) {
			PObject item = arr.get(i);
			this.decrpt(objNum, genNum, item);
		}
	}

	private void decrpt(int objNum, int genNum, PDictionary dict) throws CryptographyException,
			IOException {
		Iterator<Entry<PName, PObject>> iter = dict.entryIterator();
		while (iter.hasNext()) {
			Entry<PName, PObject> next = iter.next();
			this.decrpt(objNum, genNum, next.getValue());
		}
	}

	private void decrpt(int objNum, int genNum, PString s) throws CryptographyException,
			IOException {
		byte[] in = s.getBytes();
		byte[] out = this.securityHandler.decryptData(objNum, genNum, in);
		s.encode(out);
	}

	private void decrpt(int objNum, int genNum, PStream stream) throws CryptographyException,
			IOException {
		byte[] in = stream.getStream();
		byte[] out = this.securityHandler.decryptData(objNum, genNum, in);

		stream.setStream(out);
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
