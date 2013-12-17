package me.lumpchen.sledge.pdf.syntax.document;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class FontDescriptor extends DocObject {

	public FontDescriptor(IndirectObject obj) {
		super(obj);
	}

	@Override
	public PName getType() {
		return PName.FontDescriptor;
	}

}
