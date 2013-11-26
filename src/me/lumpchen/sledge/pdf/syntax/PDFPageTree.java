package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PInteger;
import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class PDFPageTree extends PDFDocObject {

	public PDFPageTree(IndirectObject obj) {
		super(obj);
	}
	
	public PName getType() {
		return PName.pages;
	}
	
	public PInteger getCount() {
		return this.getValueAsInteger(PName.count);
	}
	
	public PArray getKids() {
		return this.getValueAsArray(PName.kids);
	}
	
	public void getPage(int pageNo) {
		PArray kids = this.getKids();
		int size = kids.size();
		for (int i = 0; i < size; i++) {
			PObject kid = kids.getChild(i);
			if (!(kid instanceof IndirectRef)) {
				throw new SyntaxException();
			}
			// nested page tree
		}
	}
}
