package me.lumpchen.sledge.pdf.reader;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.PageContentsLoader;
import me.lumpchen.sledge.pdf.syntax.SyntaxException;
import me.lumpchen.sledge.pdf.syntax.Trailer;
import me.lumpchen.sledge.pdf.syntax.XRef;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.syntax.document.Catalog;
import me.lumpchen.sledge.pdf.syntax.document.DocumentInfo;
import me.lumpchen.sledge.pdf.syntax.document.PDFDocument;
import me.lumpchen.sledge.pdf.syntax.document.Page;
import me.lumpchen.sledge.pdf.syntax.document.PageTree;

public class PDFReader implements PageContentsLoader {

	private RandomAccessFile raf;
	private FileChannel fc;
	private SegmentedFileReader reader;
	private int pageNo;

	public PDFReader() {
	}

	void close() {
		if (fc != null) {
			try {
				fc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (raf != null) {
			try {
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public PDFDocument read(File file) throws IOException {
		this.raf = new RandomAccessFile(file, "r");
		this.fc = this.raf.getChannel();
		this.reader = new SegmentedFileReader(fc);

		PDFDocument pdfDoc = new PDFDocument();
		pdfDoc.setPageContentsLoader(this);
		this.readDocument(pdfDoc);

		return pdfDoc;
	}

	private void readDocument(PDFDocument pdfDoc) throws IOException {
		this.readTrailer(pdfDoc);
		this.readXRef(pdfDoc);
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

		IndirectObject iobj = this.readIndirectObject(entry.offset, pdfDoc);
		if (null != iobj) {
			Catalog catalog = new Catalog(iobj);
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

		IndirectObject iobj = this.readIndirectObject(entry.offset, pdfDoc);
		PageTree rootPageTree = new PageTree(iobj);
		pdfDoc.setRootPageTree(rootPageTree);
	}

	private void readPages(PageTree pageTree, PDFDocument pdfDoc) {
		if (pageTree == null) {
			return;
		}

		PArray pages = pageTree.getKids();
		int size = pages.size();
		for (int i = 0; i < size; i++) {
			PObject obj = pages.getChild(i);
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
		IndirectObject iobj = this.readIndirectObject(entry.offset, pdfDoc);
		if (iobj == null) {
			throw new SyntaxException("lost object: " + ref.toString());
		}
		
		PName type = iobj.getValueAsName(PName.type);
		if (type != null) {
			if (type == PName.page) {
				Page page = new Page(iobj);
				page.setPageNo(++pageNo);
				pageTree.addPageObject(page);
				
			} else if (type == PName.pages) {
				PageTree nestedPageTree = new PageTree(iobj);
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
			PObject obj = pageArray.getChild(i);
			readPages(obj, pageTree, pdfDoc);
		}
	}

	private IndirectObject readIndirectObject(long offset, PDFDocument pdfDoc) {
		reader.setPosition(offset);
		LineReader lineReader = new LineReader(reader);
		ObjectReader objReader = new ObjectReader(lineReader);

		IndirectObject obj = new IndirectObject();
		obj.read(objReader);
		return obj;
	}

	private void readDocumentInfo(PDFDocument pdfDoc) {
		if (pdfDoc.getTrailer() == null) {
			throw new ReadException();
		}

		Trailer trailer = pdfDoc.getTrailer();
		IndirectRef ref = trailer.getInfo();
		XRef xref = pdfDoc.getXRef();
		
		XRef.XRefEntry entry = xref.getRefEntry(ref);
		
		IndirectObject iobj = this.readIndirectObject(entry.offset, pdfDoc);
		if (null != iobj) {
			DocumentInfo docInfo = new DocumentInfo(iobj);
			pdfDoc.setDocumentInfo(docInfo);			
		}
	}

	private void readTrailer(PDFDocument pdfDoc) throws IOException {
		this.reader.setPosition(64, true);
		this.reader.setSegmentSize(64);
		LineReader lineReader = new LineReader(this.reader);
		long startxref = -1;
		
		while (true) {
			LineData line = lineReader.readLine();
			if (line == null) {
				break;
			}
			if (line.startsWith(Trailer.STARTXREF)) {
				line = lineReader.readLine();
				BytesReader breader = new BytesReader(line.getBytes());
				startxref = breader.readLong();
				break;
			}
		}
		
		if (startxref < 0) {
			throw new SyntaxException("not found startxref.");
		}
		
		reader.setPosition(startxref);
		reader.setSegmentSize(1024);
		lineReader = new LineReader(reader);
		Trailer trailer = new Trailer();
		trailer.read(lineReader);
		pdfDoc.setTrailer(trailer);
	}

	private void readXRef(PDFDocument pdfDoc) throws IOException {
		if (null == pdfDoc.getTrailer()) {
			throw new ReadException();
		}

		Trailer trailer = pdfDoc.getTrailer();
		long fp = trailer.getStartxref();
		reader.setPosition(fp);

		int size = trailer.getSize();
		// int bufSize = (size + 2) * 20;
		// reader.readSegment(bufSize);

		LineReader lineReader = new LineReader(this.reader);
		XRef xref = new XRef(size);
		xref.read(lineReader);
		pdfDoc.setXRef(xref);
	}

	@Override
	public IndirectObject loadObject(IndirectRef ref, PDFDocument pdfDoc) {
		XRef.XRefEntry entry = pdfDoc.getXRef().getRefEntry(ref);
		IndirectObject obj = this.readIndirectObject(entry.offset, pdfDoc);
		return obj;
	}
	
}
