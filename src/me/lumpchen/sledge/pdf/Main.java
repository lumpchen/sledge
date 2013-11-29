package me.lumpchen.sledge.pdf;

import java.io.File;
import java.io.IOException;

import me.lumpchen.sledge.pdf.reader.PDFReader;
import me.lumpchen.sledge.pdf.syntax.PDFDocument;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		PDFReader reader = new PDFReader();

		File f = new File("c:/mpage.pdf");
		try {
			PDFDocument doc = reader.read(f);

			System.out.println(doc.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
