package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PString;

public class DocumentInfo extends DocObject {

	public DocumentInfo(IndirectObject obj) {
		super(obj);
	}

	public PString getCreator() {
		return this.getValueAsString(PName.creator);
	}
	
	public PString getProducer() {
		return this.getValueAsString(PName.producer);
	}

	@Override
	public PName getType() {
		return null;
	}
	
}
