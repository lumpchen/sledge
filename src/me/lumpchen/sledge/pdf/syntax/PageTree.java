package me.lumpchen.sledge.pdf.syntax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class PageTree extends DocObject {
	
	private List<DocObject> objList;
	Map<Integer, Page> pageMap;
	private int count; // The number of leaf nodes(page objects)
	
	public PageTree(IndirectObject obj) {
		super(obj);
		this.objList = new ArrayList<DocObject>();
		this.pageMap = new HashMap<Integer, Page>();
		this.count = this.getValueAsNumber(PName.count).intValue();
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
	
	public int getCount() {
		return this.count;
	}
	
	public void addPageObject(DocObject obj) {
		if (obj.getType().equals(PName.page) || obj.getType().equals(PName.pages)) {
			obj.parent = this;
			this.objList.add(obj);
			
			if (obj.getType().equals(PName.page)) {
				this.pageMap.put(((Page) obj).getPageNo(), (Page) obj);
			}
			return;
		}
		
		throw new InvalidTypeException(obj.toString());
	}
	
	public Page getPage(int pageNo) {
		if (pageNo < 1 || pageNo > count) {
			throw new java.lang.IllegalArgumentException("Page number " + pageNo
					+ " exceeds page count " + count);
		}
		
		if (this.pageMap.containsKey(pageNo)) {
			return this.pageMap.get(pageNo);
		}
		
		for (DocObject obj : this.objList) {
			if (obj.getType().equals(PName.page)) {
				continue;
			}
			if (obj.getType().equals(PName.pages)) {
				PageTree subTree = (PageTree) obj;
				Page page = subTree.getPage(pageNo);
				if ( page == null) {
					continue;
				}
				return page;
			}
		}
		return null;
	}
	
	public PName getType() {
		return PName.pages;
	}
	
	public PArray getKids() {
		return this.getValueAsArray(PName.kids);
	}
	
}
