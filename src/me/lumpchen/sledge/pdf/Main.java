package me.lumpchen.sledge.pdf;

import java.io.File;
import java.io.IOException;

import me.lumpchen.sledge.pdf.reader.ContentStreamReader;
import me.lumpchen.sledge.pdf.reader.PDFReader;
import me.lumpchen.sledge.pdf.syntax.ContentStream;
import me.lumpchen.sledge.pdf.syntax.PDFDocument;
import me.lumpchen.sledge.pdf.syntax.basic.PStream;
import me.lumpchen.sledge.pdf.syntax.document.Page;
import me.lumpchen.sledge.pdf.viewer.ui.FrameViewer;
import me.lumpchen.sledge.pdf.viewer.ui.PageCanvas;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		testUI();
		testReader();
	}
	
	static void testReader() {
		PDFReader reader = new PDFReader();

		File f = new File("/c.pdf");
		try {
			PDFDocument doc = reader.read(f);

			Page page = doc.getPage(1);
			
			FrameViewer viewer = new FrameViewer();
			
			PageCanvas pageCanvas = new PageCanvas();
			pageCanvas.setPage(page);
			viewer.setPageCanvas(pageCanvas);
			
			viewer.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static void testUI() {
		PDFReader reader = new PDFReader();

		File f = new File("/p3.pdf");
		try {
			PDFDocument doc = reader.read(f);

			Page page = doc.getPage(3);
			
			System.err.println(page);
			
			FrameViewer viewer = new FrameViewer();
			
			PageCanvas pageCanvas = new PageCanvas();
			pageCanvas.setPage(page);
			viewer.setPageCanvas(pageCanvas);
			
			viewer.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
