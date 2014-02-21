package me.lumpchen.sledge.pdf;

import java.io.File;
import java.io.IOException;

import me.lumpchen.sledge.pdf.reader.PDFReader;
import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.decrypt.PDFAuthenticationFailureException;
import me.lumpchen.sledge.pdf.syntax.document.PDFDocument;
import me.lumpchen.sledge.pdf.syntax.document.Page;
import me.lumpchen.sledge.pdf.toolkit.viewer.ui.FrameViewer;
import me.lumpchen.sledge.pdf.toolkit.viewer.ui.PageCanvas;

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
			PDFDocument doc = reader.read(f);
			Page page = doc.getPage(1);
			
			System.out.println(page);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PDFAuthenticationFailureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
