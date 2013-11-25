package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.reader.LineReader;
import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.reader.SegmentedFileReader;
import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class PDFDocumentInfo {

	private IndirectObject infoObj;

	public PDFDocumentInfo() {
	}
	
	public String getCreator() {
		return this.infoObj.getValue(PName.creator).toString();
	}
	
	public String getProducer() {
		return this.infoObj.getValue(PName.producer).toString();
	}
	
	public void read(SegmentedFileReader reader) {
		LineReader lineReader = new LineReader(reader);
		ObjectReader objReader = new ObjectReader(lineReader);

		this.infoObj = new IndirectObject();
		this.infoObj.read(objReader);
	}
}
