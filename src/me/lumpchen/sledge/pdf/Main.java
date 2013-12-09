package me.lumpchen.sledge.pdf;

import java.io.File;
import java.io.IOException;

import me.lumpchen.sledge.pdf.reader.ContentStreamReader;
import me.lumpchen.sledge.pdf.reader.PDFReader;
import me.lumpchen.sledge.pdf.syntax.ContentStream;
import me.lumpchen.sledge.pdf.syntax.PDFDocument;
import me.lumpchen.sledge.pdf.syntax.Page;
import me.lumpchen.sledge.pdf.syntax.basic.PStream;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		PDFReader reader = new PDFReader();

		File f = new File("/simple.pdf");
		try {
			PDFDocument doc = reader.read(f);

			Page page = doc.getPage(1);
			
			System.out.println(page.getContents().toString());
			
			PStream stream = page.getContents().getStream();
			byte[] bstream = stream.getStream();
			
			ContentStreamReader csReader = new ContentStreamReader();
			ContentStream cs = csReader.read(bstream);
			System.err.println(cs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
