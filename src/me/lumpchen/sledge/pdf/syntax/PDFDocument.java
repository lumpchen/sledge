package me.lumpchen.sledge.pdf.syntax;

import java.io.IOException;

import me.lumpchen.sledge.pdf.reader.BytesReader;
import me.lumpchen.sledge.pdf.reader.LineData;
import me.lumpchen.sledge.pdf.reader.LineReader;
import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.reader.ReadException;
import me.lumpchen.sledge.pdf.reader.SegmentedFileReader;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class PDFDocument {

	private PDFTrailer trailer;
	private PDFXRef xref;
	private PDFDocumentInfo info;
	private PDFCatalog catalog;
	private PDFPageTree pageTree;

	public PDFDocument() {
	}

	public void setTrailer(PDFTrailer trailer) {
		this.trailer = trailer;
	}

	public PDFTrailer getTrailer() {
		return this.trailer;
	}

	public PDFDocumentInfo getInfo() {
		return this.info;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();

		if (this.pageTree != null) {
			buf.append(this.pageTree.toString());
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

	public void read(SegmentedFileReader reader) throws IOException {
		this.readTrailer(reader);
		this.readXRef(reader);

		this.readDocumentInfo(reader);

		this.readRoot(reader);

		this.readPageTree(reader);

		this.readPages(this.pageTree, reader);
	}

	private void readRoot(SegmentedFileReader reader) {
		if (this.trailer == null) {
			throw new ReadException();
		}

		IndirectRef ref = this.trailer.getRoot();
		PDFXRef.XRefEntry entry = this.xref.getRefEntry(ref);

		IndirectObject root = this.readIndirectObject(entry.offset, reader);
		if (root != null) {
			PObject obj = root.getValue(PName.type);
			if (obj != null) {
				if (PName.catalog.equals(obj)) {
					this.catalog = new PDFCatalog(root);
				}
			}
		}
	}

	private void readPageTree(SegmentedFileReader reader) {
		if (this.catalog == null) {
			return;
		}

		IndirectRef ref = this.catalog.getPages();
		PDFXRef.XRefEntry entry = this.xref.getRefEntry(ref);

		IndirectObject pages = this.readIndirectObject(entry.offset, reader);
		if (pages != null) {
			PObject obj = pages.getValue(PName.type);
			if (obj != null) {
				if (PName.pages.equals(obj)) {
					this.pageTree = new PDFPageTree(pages);
				}
			}
		}
	}

	private void readPages(PDFPageTree pageTree, SegmentedFileReader reader) {
		if (pageTree == null) {
			return;
		}

		PArray pages = pageTree.getKids();
		int size = pages.size();
		for (int i = 0; i < size; i++) {
			PObject obj = pages.getChild(i);
			this.readPage(obj, pageTree, reader);
		}
	}

	private void readPage(PObject obj, PDFPageTree pageTree, SegmentedFileReader reader) {
		if (obj == null) {
			return;
		}
		if (obj instanceof IndirectRef) {
			this.readPage((IndirectRef) obj, pageTree, reader);
		} else if (obj instanceof PArray) {
			this.readPage((PArray) obj, pageTree, reader);
		}
	}

	private void readPage(IndirectRef ref, PDFPageTree pageTree, SegmentedFileReader reader) {
		PDFXRef.XRefEntry entry = this.xref.getRefEntry(ref);
		IndirectObject iobj = this.readIndirectObject(entry.offset, reader);
		if (iobj == null) {
			throw new SyntaxException("lost object: " + ref.toString());
		}
		
		PName type = iobj.getValueAsName(PName.type);
		if (type != null) {
			if (type == PName.page) {
				pageTree.addPage(new PDFPage(iobj));
			} else if (type == PName.pages) {
				PDFPageTree nestedPageTree = new PDFPageTree(iobj);
				this.readPages(nestedPageTree, reader);
			} else {
				throw new SyntaxException("mark here");
			}
		} else {
			PObject inside = iobj.insideObj();
			if (inside == null) {
				return;
			}
			if (inside instanceof PArray) {
				this.readPage((PArray) inside, pageTree, reader);
			}
		}
	}

	private void readPage(PArray pageArray, PDFPageTree pageTree, SegmentedFileReader reader) {
		int size = pageArray.size();
		for (int i = 0; i < size; i++) {
			PObject obj = pageArray.getChild(i);
			readPage(obj, pageTree, reader);
		}
	}

	private IndirectObject readIndirectObject(long offset,
			SegmentedFileReader reader) {
		reader.setPosition(offset);
		LineReader lineReader = new LineReader(reader);
		ObjectReader objReader = new ObjectReader(lineReader);

		IndirectObject obj = new IndirectObject();
		obj.read(objReader);
		return obj;
	}

	private void readDocumentInfo(SegmentedFileReader reader) {
		if (this.trailer == null) {
			throw new ReadException();
		}
		IndirectRef ref = this.trailer.getInfo();
		PDFXRef.XRefEntry entry = this.xref.getRefEntry(ref);

		this.info = new PDFDocumentInfo();

		IndirectObject info = this.readIndirectObject(entry.offset, reader);
		this.info.setObj(info);
	}

	private void readTrailer(SegmentedFileReader reader) throws IOException {
		reader.setPosition(64, true);
		reader.setSegmentSize(64);
		LineReader lineReader = new LineReader(reader);
		long startxref = -1;
		
		while (true) {
			LineData line = lineReader.readLine();
			if (line == null) {
				break;
			}
			if (line.startsWith(PDFTrailer.STARTXREF)) {
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
		this.trailer = new PDFTrailer();
		trailer.read(lineReader);
	}

	private void readXRef(SegmentedFileReader reader) throws IOException {
		if (this.trailer == null) {
			throw new ReadException();
		}

		long fp = this.trailer.getStartxref();
		reader.setPosition(fp);

		int size = this.trailer.getSize();
		// int bufSize = (size + 2) * 20;
		// reader.readSegment(bufSize);

		LineReader lineReader = new LineReader(reader);
		this.xref = new PDFXRef(size);
		this.xref.read(lineReader);
	}
}
