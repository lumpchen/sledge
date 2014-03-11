package me.lumpchen.sledge.pdf.syntax.document;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class EncodingObj extends DocObject {

	protected EncodingObj(IndirectObject obj, PDFDocument owner) {
		super(obj, owner);
	}
	
	

	@Override
	public PName getType() {
		return PName.Encoding;
	}

}
