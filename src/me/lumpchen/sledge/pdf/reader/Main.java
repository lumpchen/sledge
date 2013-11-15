package me.lumpchen.sledge.pdf.reader;

import java.io.File;
import java.io.IOException;

import me.lumpchen.sledge.pdf.syntax.basic.PLiteralString;

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
		
		byte[] bs = new byte[]{'a', '\n', '(', 'b'};
		PLiteralString ps = new PLiteralString(bs);
		System.err.println(ps.toString());
	}
}
