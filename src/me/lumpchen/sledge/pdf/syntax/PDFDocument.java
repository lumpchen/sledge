package me.lumpchen.sledge.pdf.syntax;

import java.io.IOException;

import me.lumpchen.sledge.pdf.reader.LineReader;
import me.lumpchen.sledge.pdf.reader.ReadException;
import me.lumpchen.sledge.pdf.reader.SegmentedFileReader;

public class PDFDocument {

	private PDFTrailer trailer;
	private PDFXRef xref;

	public PDFDocument() {
	}

	public void setTrailer(PDFTrailer trailer) {
		this.trailer = trailer;
	}

	public PDFTrailer getTrailer() {
		return this.trailer;
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
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

//		int size = this.trailer.getSize();
//		int bufSize = (size + 2) * 20;
//		reader.readSegment(bufSize);
		
		LineReader lineReader = new LineReader(reader);
		this.xref = new PDFXRef();
		this.xref.read(lineReader);
	}
}
