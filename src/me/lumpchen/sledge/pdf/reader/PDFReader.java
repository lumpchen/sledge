package me.lumpchen.sledge.pdf.reader;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import me.lumpchen.sledge.pdf.syntax.PDFDocument;
import me.lumpchen.sledge.pdf.syntax.PDFTrailer;

public class PDFReader {

	private RandomAccessFile raf;
	private FileChannel fc;

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
		raf = new RandomAccessFile(file, "r");
		fc = raf.getChannel();
		SegmentedFileReader reader = new SegmentedFileReader(fc);

		PDFDocument pdfDoc = new PDFDocument();
		pdfDoc.read(reader);

		return pdfDoc;
	}
}
