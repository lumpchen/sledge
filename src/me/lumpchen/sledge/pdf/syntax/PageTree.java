package me.lumpchen.sledge.pdf.syntax;

import java.util.ArrayList;
import java.util.List;

import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PInteger;
import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class PageTree extends DocObject {
	
	private List<DocObject> objList;
	private List<Page> pageList;
	private int count;
	
	public PageTree(IndirectObject obj) {
		super(obj);
		this.objList = new ArrayList<DocObject>();
		this.pageList = new ArrayList<Page>();
		this.count = this.getValueAsInteger(PName.count).getValue();
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(super.toString());
		
		for (DocObject obj : this.objList) {
			buf.append('\n');
			buf.append(obj.toString());
		}
		
		return buf.toString();
	}
	
	public void addPageObject(DocObject obj) {
		if (obj.getType().equals(PName.page) || obj.getType().equals(PName.pages)) {
			this.objList.add(obj);
			return;
		}
		
		throw new InvalidTypeException(obj.toString());
	}
	
	public Page getPage(int pageNo) {
		if (pageNo < 1 || pageNo > count) {
			throw new java.lang.IllegalArgumentException();
		}
		return null;
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
