package me.lumpchen.sledge.pdf.syntax;

import java.io.IOException;

import me.lumpchen.sledge.pdf.reader.LineReader;
import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.reader.ReadException;
import me.lumpchen.sledge.pdf.reader.SegmentedFileReader;
import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class PDFDocument {

	private PDFTrailer trailer;
	private PDFXRef xref;
	private PDFDocumentInfo info;

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
				if (PName.CATALOG.equals(obj)) {
				}
			}
		}
	}
	
	private IndirectObject readIndirectObject(long offset, SegmentedFileReader reader) {
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
		reader.setPosition(entry.offset);
		this.info.read(reader);
	}
	
	private void readTrailer(SegmentedFileReader reader) throws IOException {
		reader.setPosition(1024, true);
		reader.setSegmentSize(1024);
		LineReader lineReader = new LineReader(reader);
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
//		int bufSize = (size + 2) * 20;
//		reader.readSegment(bufSize);
		
		LineReader lineReader = new LineReader(reader);
		this.xref = new PDFXRef(size);
		this.xref.read(lineReader);
	}
	
	private void readInfo() {
		
	}
}
