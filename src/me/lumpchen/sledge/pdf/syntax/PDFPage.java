package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class PDFPage {

	private IndirectObject page;
	private PName type;
	
	private int pageNo;
	
	public PDFPage(IndirectObject obj) {
		if (obj == null || !PName.pages.equals(obj.getValue(PName.type))) {
			throw new InvalidTypeException();
		}
		this.page = obj;
		this.type = PName.page;
	}
	
	public String toString() {
		if (this.page != null) {
			return this.page.toString();
		}
		return "";
	}

	public PName getType() {
		return this.type;
	}
	
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	public int getPageNo() {
		return this.pageNo;
	}
}
