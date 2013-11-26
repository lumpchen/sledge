package me.lumpchen.sledge.pdf.reader;

import java.io.File;
import java.io.IOException;

import me.lumpchen.sledge.pdf.syntax.PDFDocument;

public class Main {

	public static void main(String[] args) {
		PDFReader reader = new PDFReader();
		
		File f = new File("c:/itext.pdf");
		try {
			PDFDocument doc = reader.read(f);
			
			System.out.println(doc.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
