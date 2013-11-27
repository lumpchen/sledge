package me.lumpchen.sledge.pdf.syntax;

import java.util.ArrayList;
import java.util.List;

import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PInteger;
import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class PDFPageTree extends PDFDocObject {
	
	private List<PDFPage> pageList;

	public PDFPageTree(IndirectObject obj) {
		super(obj);
		
		this.pageList = new ArrayList<PDFPage>();
	}
	
	public void addPage(PDFPage page) {
		this.pageList.add(page);
	}
	
	public PDFPage getPage(int pageNo) {
		if (pageNo - 1 <= 0 || pageNo - 1 >= this.pageList.size()) {
			return null;
		}
		return this.pageList.get(pageNo - 1);
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
	
}
