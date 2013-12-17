package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.syntax.document.Page;

public interface PageContentsLoader {

	public void loadPageContents(Page page, PDFDocument pdfDoc);
	
	public void loadPageResource(Page page, PDFDocument pdfDoc);
	
}
