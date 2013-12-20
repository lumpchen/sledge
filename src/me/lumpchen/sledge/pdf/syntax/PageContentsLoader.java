package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.syntax.document.PDFDocument;

public interface PageContentsLoader {

	public IndirectObject loadObject(IndirectRef ref, PDFDocument pdfDoc);

}
