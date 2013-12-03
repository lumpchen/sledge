package me.lumpchen.sledge.pdf;

import java.io.File;
import java.io.IOException;

import me.lumpchen.sledge.pdf.reader.PDFReader;
import me.lumpchen.sledge.pdf.syntax.PDFDocument;
import me.lumpchen.sledge.pdf.syntax.Page;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		PDFReader reader = new PDFReader();

		File f = new File("c:/simple.pdf");
		try {
			PDFDocument doc = reader.read(f);

			Page page = doc.getPage(1);
			System.out.println(page.getContents().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
