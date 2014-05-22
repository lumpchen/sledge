package me.lumpchen.sledge.pdf.syntax.document;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PString;

public class DocumentInfo extends DocObject {

	public DocumentInfo(IndirectObject obj, PDFDocument owner) {
		super(obj, owner);
	}

	public PString getCreator() {
		return this.getValueAsString(PName.Creator);
	}
	
	public PString getProducer() {
		return this.getValueAsString(PName.Producer);
	}

	@Override
	public PName getType() {
		return null;
	}
	
}
