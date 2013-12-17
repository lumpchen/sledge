package me.lumpchen.sledge.pdf.syntax.document;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class Font extends DocObject {

	public Font(IndirectObject obj) {
		super(obj);
	}

	public PName getBaseFont() {
		return super.getValueAsName(PName.BaseFont);
	}
	
	@Override
	public PName getType() {
		return PName.font;
	}
}
