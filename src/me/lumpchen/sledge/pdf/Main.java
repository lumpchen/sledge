package me.lumpchen.sledge.pdf;

import java.io.File;
import java.io.IOException;

import me.lumpchen.sledge.pdf.reader.PDFAuthenticationFailureException;
import me.lumpchen.sledge.pdf.reader.PDFReader;
import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.PDFFile;
import me.lumpchen.sledge.pdf.syntax.document.Annot;
import me.lumpchen.sledge.pdf.syntax.document.PDFDocument;
import me.lumpchen.sledge.pdf.syntax.document.Page;
import me.lumpchen.sledge.pdf.syntax.lang.PArray;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testReader();
	}

	static void testReader() {
		PDFReader reader = new PDFReader();

		File f = new File("C:/pdfs/signature_2.pdf");
		try {
			PDFFile pdf = new PDFFile(f);

			PDFDocument doc = reader.read(pdf);
			Page page = doc.getPage(1);

			PArray fields = doc.getCatalog().getFields();
			for (int i = 0; i < fields.size(); i++) {
				Annot annot = doc.getCatalog().getAnnot(((IndirectRef) fields.get(i)));
				System.out.println(annot);
			}
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
