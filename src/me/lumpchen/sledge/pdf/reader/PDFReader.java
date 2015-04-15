package me.lumpchen.sledge.pdf.reader;

import java.io.IOException;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.ObjectStream;
import me.lumpchen.sledge.pdf.syntax.PDFFile;
import me.lumpchen.sledge.pdf.syntax.PageContentsLoader;
import me.lumpchen.sledge.pdf.syntax.SyntaxException;
import me.lumpchen.sledge.pdf.syntax.Trailer;
import me.lumpchen.sledge.pdf.syntax.XRef;
import me.lumpchen.sledge.pdf.syntax.document.Catalog;
import me.lumpchen.sledge.pdf.syntax.document.DocumentInfo;
import me.lumpchen.sledge.pdf.syntax.document.PDFDocument;
import me.lumpchen.sledge.pdf.syntax.document.Page;
import me.lumpchen.sledge.pdf.syntax.document.PageTree;
import me.lumpchen.sledge.pdf.syntax.ecryption.BadSecurityHandlerException;
import me.lumpchen.sledge.pdf.syntax.ecryption.CryptographyException;
import me.lumpchen.sledge.pdf.syntax.lang.PArray;
import me.lumpchen.sledge.pdf.syntax.lang.PName;
import me.lumpchen.sledge.pdf.syntax.lang.PObject;
import me.lumpchen.sledge.pdf.syntax.lang.PStream;

public class PDFReader implements PageContentsLoader {

	private FileBufferedRandomByteReader reader;
	private int pageNo;
	private PDFFile pdfFile;

	public PDFReader() {
	}

	public PDFDocument read(PDFFile file) throws IOException, PDFAuthenticationFailureException {
		this.pdfFile = file;
		this.reader = new FileBufferedRandomByteReader(this.pdfFile.getFileChannel());

		PDFDocument pdfDoc = new PDFDocument();
		pdfDoc.setPageContentsLoader(this);
		this.readDocument(pdfDoc);

		return pdfDoc;
	}

	private void readDocument(PDFDocument pdfDoc) throws IOException,
			PDFAuthenticationFailureException {
		this.readTrailer(pdfDoc);
		this.readXRef(pdfDoc);

		this.checkSecurity(pdfDoc);

		this.readDocumentInfo(pdfDoc);
		this.readRoot(pdfDoc);
		this.readPageTree(pdfDoc);
		this.readPages(pdfDoc.getRootPageTree(), pdfDoc);
	}

	private void readRoot(PDFDocument pdfDoc) {
		if (pdfDoc.getTrailer() == null) {
			throw new ReadException();
		}

		Trailer trailer = pdfDoc.getTrailer();
		IndirectRef ref = trailer.getRoot();

		XRef xref = pdfDoc.getXRef();
		XRef.XRefEntry entry = xref.getRefEntry(ref);

		IndirectObject iobj = this.readIndirectObject(entry, pdfDoc);
		if (null != iobj) {
			Catalog catalog = new Catalog(iobj, pdfDoc);
			pdfDoc.setCatalog(catalog);
		}
	}

	private void readPageTree(PDFDocument pdfDoc) {
		if (pdfDoc.getCatalog() == null) {
			return;
		}

		Catalog catalog = pdfDoc.getCatalog();
		IndirectRef ref = catalog.getPages();

		XRef xref = pdfDoc.getXRef();
		XRef.XRefEntry entry = xref.getRefEntry(ref);

		IndirectObject iobj = this.readIndirectObject(entry, pdfDoc);
		PageTree rootPageTree = new PageTree(iobj, pdfDoc);
		pdfDoc.setRootPageTree(rootPageTree);
	}

	private void readPages(PageTree pageTree, PDFDocument pdfDoc) {
		if (pageTree == null) {
			return;
		}

		PArray pages = pageTree.getKids();
		int size = pages.size();
		for (int i = 0; i < size; i++) {
			PObject obj = pages.get(i);
			this.readPages(obj, pageTree, pdfDoc);
		}
	}

	private void readPages(PObject obj, PageTree pageTree, PDFDocument pdfDoc) {
		if (obj == null) {
			return;
		}
		if (obj instanceof IndirectRef) {
			this.readPage((IndirectRef) obj, pageTree, pdfDoc);
		} else if (obj instanceof PArray) {
			this.readPage((PArray) obj, pageTree, pdfDoc);
		}
	}

	private void readPage(IndirectRef ref, PageTree pageTree, PDFDocument pdfDoc) {
		XRef.XRefEntry entry = pdfDoc.getXRef().getRefEntry(ref);
		IndirectObject iobj = this.readIndirectObject(entry, pdfDoc);
		if (iobj == null) {
			throw new SyntaxException("lost object: " + ref.toString());
		}

		PName type = iobj.getValueAsName(PName.Type);
		if (type != null) {
			if (type == PName.Page) {
				Page page = new Page(iobj, pdfDoc);
				page.setPageNo(++pageNo);
				pageTree.addPageObject(page);

			} else if (type == PName.Pages) {
				PageTree nestedPageTree = new PageTree(iobj, pdfDoc);
				this.readPages(nestedPageTree, pdfDoc);

				pageTree.addPageObject(nestedPageTree);
			} else {
				throw new SyntaxException("mark here");
			}
		} else {
			PObject inside = iobj.insideObj();
			if (inside == null) {
				return;
			}
			if (inside instanceof PArray) {
				this.readPage((PArray) inside, pageTree, pdfDoc);
			}
		}
	}

	private void readPage(PArray pageArray, PageTree pageTree, PDFDocument pdfDoc) {
		int size = pageArray.size();
		for (int i = 0; i < size; i++) {
			PObject obj = pageArray.get(i);
			readPages(obj, pageTree, pdfDoc);
		}
	}

	private void readDocumentInfo(PDFDocument pdfDoc) {
		if (pdfDoc.getTrailer() == null) {
			throw new ReadException();
		}

		Trailer trailer = pdfDoc.getTrailer();
		XRef xref = pdfDoc.getXRef();

		IndirectRef ref = trailer.getInfo();
		if (ref != null) {
			XRef.XRefEntry entry = xref.getRefEntry(ref);

			IndirectObject iobj = this.readIndirectObject(entry, pdfDoc);
			if (null != iobj) {
				DocumentInfo docInfo = new DocumentInfo(iobj, pdfDoc);
				pdfDoc.setDocumentInfo(docInfo);
			}			
		}
	}

	private void readTrailer(PDFDocument pdfDoc) throws IOException,
			PDFAuthenticationFailureException {
		this.reader.position(64, true);
		Tokenizer tokenizer = new Tokenizer(this.reader);
		long startxref = -1;

		while (true) {
			LineData line = new LineData(tokenizer.readLine());
			if (line.length() == 0) {
				break;
			}
			if (line.startsWith(Trailer.STARTXREF)) {
				line = new LineData(tokenizer.readLine());
				BytesReader breader = new BytesReader(line.getBytes());
				startxref = breader.readLong();
				break;
			}
		}

		if (startxref < 0) {
			throw new SyntaxException("not found startxref.");
		}

		this.readTrailer(pdfDoc, startxref, null);
	}

	private void readTrailer(PDFDocument pdfDoc, long startxref, Trailer current)
			throws IOException, PDFAuthenticationFailureException {
		reader.position(startxref);
		Trailer trailer = new Trailer();
		if (!trailer.read(this.reader)) {
			IndirectObject obj = this.readIndirectObject(startxref, pdfDoc);
			if (null == obj || obj.getStream() == null
					|| !obj.getValueAsName(PName.Type).equals(PName.XRef)) {
				throw new SyntaxException("not found xref stream: " + trailer.toString());
			}
			trailer.setXRefObj(obj);
		}
		if (null == current) {
			pdfDoc.setTrailer(trailer);
		} else {
			current.setPreTrailer(trailer);
		}

		trailer.setStartxref(startxref);

		long prev = trailer.getPrev();
		if (prev > 0) {
			this.readTrailer(pdfDoc, prev, trailer);
		}
	}

	private void readXRef(PDFDocument pdfDoc) throws IOException {
		if (null == pdfDoc.getTrailer()) {
			throw new ReadException("Not found trailer.");
		}

		Trailer trailer = pdfDoc.getTrailer();
		this.readXRef(pdfDoc, trailer, null);
	}

	private void readXRef(PDFDocument pdfDoc, Trailer trailer, XRef xref) throws IOException {
		long fp = trailer.getStartxref();
		this.reader.position(fp);

		// int size = trailer.getSize();

		if (null == xref) {
			xref = new XRef();
			pdfDoc.setXRef(xref);
		}

		if (!xref.read(this.reader)) {
			IndirectObject obj = this.readIndirectObject(fp, pdfDoc);
			if (null == obj || obj.getStream() == null
					|| !obj.getValueAsName(PName.Type).equals(PName.XRef)) {
				throw new SyntaxException("not found xref stream: " + trailer.toString());
			}
			xref.readStream(obj.getStream());
		}

		long xrefStream = trailer.getXRefStm();
		if (xrefStream > 0) {
			IndirectObject refStmObj = this.readIndirectObject(xrefStream, pdfDoc);
			if (null == refStmObj) {
				throw new SyntaxException("not found xref stream: " + trailer.toString());
			}
			PStream refStream = refStmObj.getStream();
			xref.readStream(refStream);
		}

		if (trailer.getPrevTrailer() != null) {
			this.readXRef(pdfDoc, trailer.getPrevTrailer(), xref);
		}
	}

	public void checkSecurity(PDFDocument pdfDoc) throws PDFAuthenticationFailureException {
		byte[] password = this.pdfFile.getPassword();
		String keyStore = this.pdfFile.getKeyStore();
		String alias = this.pdfFile.getAlias();
		try {
			pdfDoc.checkSecurity(password, keyStore, alias);
		} catch (BadSecurityHandlerException | IOException e) {
			e.printStackTrace();
		} catch (CryptographyException e) {
			if (e.causedByWrongPassword()) {
				e.printStackTrace();
				throw new PDFAuthenticationFailureException("Password is wrong.");
			} else {
				e.printStackTrace();
			}
		}
	}

	@Override
	public IndirectObject loadObject(IndirectRef ref, PDFDocument pdfDoc) throws ReadException {
		XRef.XRefEntry entry = pdfDoc.getXRef().getRefEntry(ref);

		IndirectObject obj = this.readIndirectObject(entry, pdfDoc);
		
		if (!entry.inObjectStream && !PName.XRef.equals(obj.getValueAsName(PName.Type))) {
			try {
				pdfDoc.decrypt(obj);
			} catch (CryptographyException | IOException e) {
				throw new ReadException(e);
			}	
		}
		
		return obj;
	}

	private IndirectObject readIndirectObject(XRef.XRefEntry entry, PDFDocument pdfDoc) {
		IndirectObject obj = null;
		if (entry.inObjectStream) {
			IndirectRef ref = new IndirectRef(entry.objNum, 0);

			int objStreamNum = entry.objStreamNum;
			IndirectRef osRef = new IndirectRef(objStreamNum, entry.genNum);

			ObjectStream os = pdfDoc.getObjStream(osRef);
			if (null == os) {
				IndirectObject oStream = this.loadObject(osRef, pdfDoc);

				if (null == oStream) {
					throw new SyntaxException("not found obj: " + osRef);
				}

				PStream stream = oStream.getStream();
				os = new ObjectStream(stream);
				pdfDoc.pushObjStream(osRef, os);
			}
			obj = this.readIndirectObject(ref, os.getContent(entry.objNum), pdfDoc);
		} else {
			obj = this.readIndirectObject(entry.offset, pdfDoc);
		}
		return obj;
	}

	private IndirectObject readIndirectObject(long offset, PDFDocument pdfDoc) {
		reader.position(offset);

		ObjectReader objReader = new ObjectReader(reader);
		IndirectObject obj = objReader.readIndirectObject(pdfDoc);
		return obj;
	}

	private IndirectObject readIndirectObject(IndirectRef ref, byte[] data, PDFDocument pdfDoc) {
		IndirectObject obj = new IndirectObject(ref.getObjNum(), ref.getGenNum());

		ObjectReader objReader = new ObjectReader(data);
		PObject insideObj = objReader.readNextObj();

		if (insideObj != null) {
			obj.setInsideObj(insideObj);
		}
		return obj;
	}
}
