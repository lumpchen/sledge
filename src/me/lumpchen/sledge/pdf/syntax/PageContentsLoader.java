package me.lumpchen.sledge.pdf.syntax;

public interface PageContentsLoader {

	public IndirectObject loadObject(IndirectRef ref, PDFDocument pdfDoc);

}
