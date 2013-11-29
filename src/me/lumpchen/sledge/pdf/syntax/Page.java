package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class Page extends DocObject {

	private int pageNo;
	
	public Page(IndirectObject obj) {
		super(obj);
	}
	
	public PName getType() {
		return PName.page;
	}
	
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	public int getPageNo() {
		return this.pageNo;
	}
}
