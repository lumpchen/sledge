package me.lumpchen.sledge.pdf.syntax;

import java.io.IOException;
import java.nio.ByteBuffer;

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

	public void read(SegmentedFileReader reader) throws IOException {
		this.readTrailer(reader);
		this.readXRef(reader);
	}

	private void readTrailer(SegmentedFileReader reader) throws IOException {
		ByteBuffer buf = reader.readBufferFromTrailer(2048);
		LineReader lineReader = new LineReader(buf, true);
		this.trailer = new PDFTrailer();
		trailer.read(lineReader);
	}

	private void readXRef(SegmentedFileReader reader) throws IOException {
		if (this.trailer == null) {
			throw new ReadException();
		}
		
		long fp = this.trailer.getStartxref();
		int size = this.trailer.getSize();
		
		int bufSize = (size + 2) * 20;
		ByteBuffer buf = reader.readBuffer(fp, bufSize);
		
		LineReader lineReader = new LineReader(buf);
		this.xref = new PDFXRef();
		this.xref.read(lineReader);
	}
}
