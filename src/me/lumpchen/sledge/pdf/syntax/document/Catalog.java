package me.lumpchen.sledge.pdf.syntax.document;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.lang.PName;

public class Catalog extends DocObject {

	public Catalog(IndirectObject obj, PDFDocument owner) {
		super(obj, owner);
	}

	public PName getType() {
		return PName.Catalog;
	}

	public IndirectRef getPages() {
		return this.getValueAsRef(PName.Pages);
	}
}
