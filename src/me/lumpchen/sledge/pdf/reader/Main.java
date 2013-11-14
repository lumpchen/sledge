package me.lumpchen.sledge.pdf.reader;

import java.io.File;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		PDFReader reader = new PDFReader();
		
		File f = new File("c:/itext.pdf");
		try {
			reader.read(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
