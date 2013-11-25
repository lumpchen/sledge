package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.reader.LineReader;
import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.reader.SegmentedFileReader;
import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class PDFCatalog {

	private IndirectObject infoObj;
	private PName Type;
	
	public PDFCatalog() {
		this.Type = PName.catalog;
	}
	
	public IndirectRef getPages() {
		return (IndirectRef) this.infoObj.getValue(PName.pages);
	}
	
	public PName getNType() {
		return (PName) this.infoObj.getValue(PName.type);
	}
	
	public void read(SegmentedFileReader reader) {
		LineReader lineReader = new LineReader(reader);
		ObjectReader objReader = new ObjectReader(lineReader);

		this.infoObj = new IndirectObject();
		this.infoObj.read(objReader);
	}
}
