package me.lumpchen.sledge.pdf.syntax.document;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;

public class FontObject extends DocObject {

	public FontObject(IndirectObject obj) {
		super(obj);
	}

	public PName getBaseFont() {
		return super.getValueAsName(PName.BaseFont);
	}
	
	@Override
	public PName getType() {
		return PName.font;
	}
	
	public PName getSubType() {
		return this.getValueAsName(PName.Subtype);
	}
	
	public PNumber getFirstChar() {
		return this.getValueAsNumber(PName.FirstChar);
	}
	
	public PNumber getLastChar() {
		return this.getValueAsNumber(PName.LastChar);
	}
	
	public PArray getWidths() {
		return this.getValueAsArray(PName.Widths);
	}
	
	public PDictionary getFontDescriptor() {
		return this.getValueAsDict(PName.FontDescriptor);
	}
	
}
