package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class Catalog extends DocObject {

	public Catalog(IndirectObject obj) {
		super(obj);
	}
	
	public PName getType() {
		return PName.catalog;
	}
	
	public IndirectRef getPages() {
		return this.getValueAsRef(PName.pages);
	}
}
