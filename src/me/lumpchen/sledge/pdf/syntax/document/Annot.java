package me.lumpchen.sledge.pdf.syntax.document;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.lang.PName;

public class Annot extends DocObject {
	
	public Annot(IndirectObject obj, PDFDocument owner) {
		super(obj, owner);
	}

	public PName getType() {
		return PName.Annot;
	}
}
