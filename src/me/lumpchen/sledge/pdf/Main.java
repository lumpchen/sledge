package me.lumpchen.sledge.pdf;

import java.io.File;
import java.io.IOException;

import me.lumpchen.sledge.pdf.reader.PDFReader;
import me.lumpchen.sledge.pdf.syntax.PDFFile;
import me.lumpchen.sledge.pdf.syntax.decrypt.PDFAuthenticationFailureException;
import me.lumpchen.sledge.pdf.syntax.document.PDFDocument;
import me.lumpchen.sledge.pdf.syntax.document.Page;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testReader();
	}
	
	static void testReader() {
		PDFReader reader = new PDFReader();

		File f = new File("/xref_stream.pdf");
		try {
			PDFFile pdf = new PDFFile(f);
			
			PDFDocument doc = reader.read(pdf);
			Page page = doc.getPage(1);

			pdf.close();
			System.out.println(page);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PDFAuthenticationFailureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
